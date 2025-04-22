package com.manxiaozhi.javadocmark;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.lang.model.element.*;
import javax.lang.model.type.*;
import javax.lang.model.util.Elements;

import jdk.javadoc.doclet.DocletEnvironment;

/**
 * Builder that creates markdown file for class documentation.
 * 
 * @author wangzhi
 */
public final class JavadocMarkDocumentBuilder extends MarkdownDocumentBuilder {

	/** Target type element this builder is working on. **/
	private final TypeElement typeElement;

	/** Doclet environment instance. **/
	private final DocletEnvironment environment;

	/**
	 * Default constructor. 
	 * 
	 * @param typeElement Target type element this builder is working on.
	 * @param environment Doclet environment instance.
	 */
	public JavadocMarkDocumentBuilder(final TypeElement typeElement, final DocletEnvironment environment) {
		this.typeElement = typeElement;
		this.environment = environment;
	}

	/**
	 * Builds and returns the package name of the given type.
	 * 
	 * @param type Type to get package name from.
	 * @return Package name of the given type.
	 */
	private String getPackageName(final TypeMirror type) {
		if (type instanceof DeclaredType) {
			Element element = ((DeclaredType) type).asElement();
			if (element != null) {
				Element enclosing = element.getEnclosingElement();
				if (enclosing instanceof PackageElement) {
					return ((PackageElement) enclosing).getQualifiedName().toString();
				}
			}
		}
		return "";
	}

	/**
	 * Builds and returns the class name of the given type.
	 * 
	 * @param type Type to get class name from.
	 * @return Class name of the given type.
	 */
	private String getClassName(final TypeMirror type) {
		if (type instanceof DeclaredType) {
			Element element = ((DeclaredType) type).asElement();
			if (element != null) {
				return element.getSimpleName().toString();
			}
		}
		return type.toString();
	}

	/**
	 * Process the given annotation value and returns
	 * a valid markdown link if possible.
	 * 
	 * @param value Annotation value to process.
	 * @return Optional link built from value if possible.
	 */
	private Optional<String> processAnnotationValue(final AnnotationValue value) {
		if (value == null) {
			return Optional.empty();
		}
		Object object = value.getValue();
		if (object instanceof TypeMirror) {
			TypeMirror type = (TypeMirror) object;
			return Optional.of(buildTypeLink(type));
		}
		return Optional.of(object.toString());
	}

	/**
	 * Process the given element and returns a valid
	 * markdown link if possible.
	 * 
	 * @param element Element to process.
	 * @return Optional link built from element if possible.
	 */
	private Optional<String> processElement(final Element element) {
		if (element.getKind() == ElementKind.METHOD) {
			return Optional.of(buildMethodLink((ExecutableElement) element));
		}
		return Optional.empty();
	}

	/**
	 * Builds a valid markdown link for the given method.
	 * 
	 * @param method Method to build link for.
	 * @return Built link.
	 */
	private String buildMethodLink(final ExecutableElement method) {
		final StringBuilder builder = new StringBuilder();
		builder.append(method.getSimpleName().toString());
		builder.append('(');
		final List<? extends VariableElement> parameters = method.getParameters();
		if (!parameters.isEmpty()) {
			builder.append(parameters.stream()
				.map(param -> buildTypeLink(param.asType()))
				.collect(Collectors.joining(", ")));
		}
		builder.append(')');
		return builder.toString();
	}

	/**
	 * Builds a valid markdown link for the given type.
	 * 
	 * @param type Type to build link for.
	 * @return Built link.
	 */
	private String buildTypeLink(final TypeMirror type) {
		final String packageName = getPackageName(type);
		final String className = getClassName(type);
		if (packageName.isEmpty()) {
			return className;
		}
		return String.format("[%s](%s/%s.md)", className, packageName.replace('.', '/'), className);
	}

	/**
	 * Builds documentation for the given method.
	 * 
	 * @param method Method to build documentation for.
	 * @throws IOException If any error occurs while writing documentation.
	 */
	private void buildMethodDocumentation(final ExecutableElement method) throws IOException {
		if (method.getKind() == ElementKind.METHOD) {
			header(3, method.getSimpleName().toString());
			final String comment = environment.getElementUtils().getDocComment(method);
			if (comment != null) {
				text(comment);
			}
			buildParametersDocumentation(method);
			buildReturnDocumentation(method);
			buildThrowsDocumentation(method);
		}
	}

	/**
	 * Builds parameters documentation for the given method.
	 * 
	 * @param method Method to build parameters documentation for.
	 * @throws IOException If any error occurs while writing documentation.
	 */
	private void buildParametersDocumentation(final ExecutableElement method) throws IOException {
		final List<? extends VariableElement> parameters = method.getParameters();
		if (!parameters.isEmpty()) {
			header(4, "Parameters");
			for (VariableElement param : parameters) {
				text("* " + param.getSimpleName() + " - " + buildTypeLink(param.asType()));
				newLine();
			}
		}
	}
	
	/**
	 * Builds return type documentation for the given method.
	 * 
	 * @param method Method to build return type documentation for.
	 * @throws IOException If any error occurs while writing documentation.
	 */
	private void buildReturnDocumentation(final ExecutableElement method) throws IOException {
		final TypeMirror returnType = method.getReturnType();
		if (returnType.getKind() != TypeKind.VOID) {
			header(4, "Returns");
			text(buildTypeLink(returnType));
		}
	}

	/**
	 * Builds throws documentation for the given method.
	 * 
	 * @param method Method to build throws documentation for.
	 * @throws IOException If any error occurs while writing documentation.
	 */
	private void buildThrowsDocumentation(final ExecutableElement method) throws IOException {
		final List<? extends TypeMirror> thrownTypes = method.getThrownTypes();
		if (!thrownTypes.isEmpty()) {
			header(4, "Throws");
			for (TypeMirror type : thrownTypes) {
				text("* " + buildTypeLink(type));
				newLine();
			}
		}
	}

	/**
	 * Builds class header by writing class name and comment.
	 * 
	 * @throws IOException If any error occurs while writing header.
	 */
	private void buildClassHeader() throws IOException {
		header(1, typeElement.getSimpleName().toString());
		final String comment = environment.getElementUtils().getDocComment(typeElement);
		if (comment != null) {
			text(comment);
		}
	}

	/**
	 * Builds method list by processing each method of our target class.
	 * 
	 * @throws IOException If any error occurs while writing method list.
	 */
	private void buildMethodList() throws IOException {
		final List<Element> methods = typeElement.getEnclosedElements().stream()
			.filter(element -> element.getKind() == ElementKind.METHOD)
			.collect(Collectors.toList());
		if (!methods.isEmpty()) {
			header(2, "Methods");
			methods.forEach(method -> {
				try {
					buildMethodDocumentation((ExecutableElement) method);
				}
				catch (final IOException e) {
					e.printStackTrace();
				}
			});
		}
	}

	/** {@inheritDoc} **/
	@Override
	public void build(final Path output) throws IOException {
		buildClassHeader();
		buildMethodList();
		super.build(output);
	}

}
