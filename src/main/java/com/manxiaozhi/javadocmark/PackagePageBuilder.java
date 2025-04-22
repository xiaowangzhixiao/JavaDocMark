package com.manxiaozhi.javadocmark;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

import jdk.javadoc.doclet.DocletEnvironment;

/**
 * Builder that creates package documentation file in Markdown format.
 * This class handles the generation of package-level documentation including:
 * - Package header with name and description
 * - Table of contained classes with their descriptions
 * 
 * @author wangzhi
 */
public final class PackagePageBuilder extends MarkdownDocumentBuilder {

	/** Target package element this builder is working on. **/
	private final PackageElement packageElement;

	/** Doclet environment instance for accessing documentation utilities. **/
	private final DocletEnvironment environment;

	/** Cached list of classes in the package. **/
	private final List<TypeElement> classes;

	/** Utility for accessing element documentation. **/
	private final Elements elementUtils;

	/**
	 * Creates a new package page builder.
	 * 
	 * @param packageElement Package to build documentation for
	 * @param environment Doclet environment instance
	 */
	public PackagePageBuilder(final PackageElement packageElement, final DocletEnvironment environment) {
		this.packageElement = packageElement;
		this.environment = environment;
		this.elementUtils = environment.getElementUtils();
		this.classes = initializeClassList();
	}

	/**
	 * Initializes the list of classes in the package.
	 * 
	 * @return List of TypeElements representing classes in the package
	 */
	private List<TypeElement> initializeClassList() {
		return environment.getIncludedElements().stream()
			.filter(element -> element.getKind() == ElementKind.CLASS || 
							 element.getKind() == ElementKind.INTERFACE || 
							 element.getKind() == ElementKind.ENUM)
			.map(element -> (TypeElement) element)
			.filter(element -> element.getEnclosingElement().equals(packageElement))
			.collect(Collectors.toList());
	}

	/**
	 * Builds package header by writing package name and documentation.
	 * 
	 * @throws IOException If any error occurs while writing header
	 */
	private void buildPackageHeader() throws IOException {
		System.out.println("[PackagePageBuilder] Building package header");
		header(1, packageElement.getQualifiedName().toString());
		String comment = elementUtils.getDocComment(packageElement);
		if (comment != null) {
			System.out.println("[PackagePageBuilder] Found package comment");
			text(comment);
			newLine();
		} else {
			System.out.println("[PackagePageBuilder] No package comment found");
		}
	}

	/**
	 * Extracts the first sentence from the given documentation comment.
	 * 
	 * @param comment Documentation comment to extract from.
	 * @return First sentence of the comment, or empty string if none.
	 */
	private String getFirstSentence(final String comment) {
		if (comment == null || comment.isEmpty()) {
			return "";
		}
		// 去掉所有回车和换行符
		String text = comment.replaceAll("[\r\n]+", " ");
		// 找到第一个句号、问号或感叹号
		int endIndex = -1;
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (c == '.' || c == '?' || c == '!') {
				endIndex = i + 1;
				break;
			}
		}
		return endIndex > 0 ? text.substring(0, endIndex).trim() : text.trim();
	}

	/**
	 * Builds and writes class table if the package contains classes.
	 * 
	 * @throws IOException If any error occurs while writing class table
	 */
	private void buildClassTable() throws IOException {
		System.out.println("[PackagePageBuilder] Building class table with " + classes.size() + " types");
		if (!classes.isEmpty()) {
			header(2, "Types");
			text("| Name | Kind | Description |");
			newLine();
			text("|------|------|-------------|");
			newLine();
			for (TypeElement classElement : classes) {
				String className = classElement.getSimpleName().toString();
				String kind = classElement.getKind().toString().toLowerCase();
				String description = getFirstSentence(elementUtils.getDocComment(classElement));
				String fileName = getTypeFileName(classElement);
				System.out.println("[PackagePageBuilder] Processing " + kind + ": " + className);
				text(String.format("| [%s](%s.md) | %s | %s |", 
					className, fileName, kind, description));
				newLine();
			}
		} else {
			System.out.println("[PackagePageBuilder] No types found in package");
		}
	}

	private String getTypeFileName(TypeElement element) {
		String fileName = element.getSimpleName().toString();
		if (element.getNestingKind().isNested()) {
			// 获取外部类
			TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
			while (enclosingElement != null && enclosingElement.getNestingKind().isNested()) {
				fileName = enclosingElement.getSimpleName() + "." + fileName;
				enclosingElement = (TypeElement) enclosingElement.getEnclosingElement();
			}
			if (enclosingElement != null) {
				fileName = enclosingElement.getSimpleName() + "." + fileName;
			}
		}
		return fileName;
	}

	/** 
	 * Builds the complete package documentation file.
	 *
	 * @param output Path where the documentation file should be written
	 * @throws IOException If any error occurs while building the documentation
	 */
	@Override
	public void build(final Path output) throws IOException {
		System.out.println("[PackagePageBuilder] Building documentation for package: " + packageElement.getQualifiedName());
		try {
			buildPackageHeader();
			System.out.println("[PackagePageBuilder] Package header built successfully");
			
			buildClassTable();
			System.out.println("[PackagePageBuilder] Class table built successfully");
			
			super.build(output);
			System.out.println("[PackagePageBuilder] Documentation written to: " + output);
		} catch (IOException e) {
			System.err.println("[PackagePageBuilder] Error building documentation: " + e.getMessage());
			throw e;
		}
	}

}
