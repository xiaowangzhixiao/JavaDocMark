package com.manxiaozhi.javadocmark;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

import jdk.javadoc.doclet.DocletEnvironment;

/**
 * Builder that creates class documentation file.
 * 
 * @author wangzhi
 */
public final class ClassPageBuilder extends MarkdownDocumentBuilder {

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
    public ClassPageBuilder(final TypeElement typeElement, final DocletEnvironment environment) {
        this.typeElement = typeElement;
        this.environment = environment;
    }

    /**
     * Gets the full name of the type, including outer class names for nested types.
     * 
     * @return The full name of the type.
     */
    private String getTypeFullName() {
        String name = typeElement.getSimpleName().toString();
        if (typeElement.getNestingKind().isNested()) {
            // 获取外部类
            TypeElement enclosingElement = (TypeElement) typeElement.getEnclosingElement();
            while (enclosingElement != null && enclosingElement.getNestingKind().isNested()) {
                name = enclosingElement.getSimpleName() + "." + name;
                enclosingElement = (TypeElement) enclosingElement.getEnclosingElement();
            }
            if (enclosingElement != null) {
                name = enclosingElement.getSimpleName() + "." + name;
            }
        }
        return name;
    }

    /**
     * Builds class header by writing class name and comment.
     * 
     * @throws IOException If any error occurs while writing header.
     */
    private void buildClassHeader() throws IOException {
        System.out.println("[ClassPageBuilder] Building class header");
        header(1, getTypeFullName());
        final String comment = environment.getElementUtils().getDocComment(typeElement);
        if (comment != null) {
            System.out.println("[ClassPageBuilder] Found class comment");
            text(comment);
            newLine();
        } else {
            System.out.println("[ClassPageBuilder] No class comment found");
        }
    }

    /**
     * Builds field documentation for the given field.
     * 
     * @param field Field to build documentation for.
     * @throws IOException If any error occurs while writing documentation.
     */
    private void buildFieldDocumentation(final VariableElement field) throws IOException {
        header(3, field.getSimpleName().toString());
        final String comment = environment.getElementUtils().getDocComment(field);
        if (comment != null) {
            text(comment);
            newLine();
        }
    }

    /**
     * Builds field list by processing each field of our target class.
     * 
     * @throws IOException If any error occurs while writing field list.
     */
    private void buildFieldList() throws IOException {
        System.out.println("[ClassPageBuilder] Building field list");
        final List<VariableElement> fields = typeElement.getEnclosedElements().stream()
            .filter(element -> element.getKind() == ElementKind.FIELD)
            .map(element -> (VariableElement) element)
            .collect(Collectors.toList());
        System.out.println("[ClassPageBuilder] Found " + fields.size() + " fields");
        
        if (!fields.isEmpty()) {
            header(2, "Fields");
            for (VariableElement field : fields) {
                System.out.println("[ClassPageBuilder] Processing field: " + field.getSimpleName());
                buildFieldDocumentation(field);
            }
        }
    }

    /**
     * Builds method documentation for the given method.
     * 
     * @param method Method to build documentation for.
     * @throws IOException If any error occurs while writing documentation.
     */
    private void buildMethodDocumentation(final ExecutableElement method) throws IOException {
        header(3, method.getSimpleName().toString());
        final String comment = environment.getElementUtils().getDocComment(method);
        if (comment != null) {
            text(comment);
            newLine();
        }
        buildParametersDocumentation(method);
        buildReturnDocumentation(method);
        buildThrowsDocumentation(method);
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
                text("* " + param.getSimpleName() + " - " + param.asType().toString());
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
        if (method.getReturnType() != null) {
            header(4, "Returns");
            text(method.getReturnType().toString());
            newLine();
        }
    }

    /**
     * Builds throws documentation for the given method.
     * 
     * @param method Method to build throws documentation for.
     * @throws IOException If any error occurs while writing documentation.
     */
    private void buildThrowsDocumentation(final ExecutableElement method) throws IOException {
        final List<? extends TypeElement> thrownTypes = method.getThrownTypes().stream()
            .map(type -> (TypeElement) environment.getTypeUtils().asElement(type))
            .collect(Collectors.toList());
        if (!thrownTypes.isEmpty()) {
            header(4, "Throws");
            for (TypeElement type : thrownTypes) {
                text("* " + type.getQualifiedName().toString());
                newLine();
            }
        }
    }

    /**
     * Builds method list by processing each method of our target class.
     * 
     * @throws IOException If any error occurs while writing method list.
     */
    private void buildMethodList() throws IOException {
        System.out.println("[ClassPageBuilder] Building method list");
        final List<ExecutableElement> methods = typeElement.getEnclosedElements().stream()
            .filter(element -> element.getKind() == ElementKind.METHOD)
            .map(element -> (ExecutableElement) element)
            .collect(Collectors.toList());
        System.out.println("[ClassPageBuilder] Found " + methods.size() + " methods");
        
        if (!methods.isEmpty()) {
            header(2, "Methods");
            for (ExecutableElement method : methods) {
                System.out.println("[ClassPageBuilder] Processing method: " + method.getSimpleName());
                buildMethodDocumentation(method);
            }
        }
    }

    /** {@inheritDoc} **/
    @Override
    public void build(final Path output) throws IOException {
        System.out.println("[ClassPageBuilder] Building documentation for class: " + typeElement.getQualifiedName());
        try {
            buildClassHeader();
            System.out.println("[ClassPageBuilder] Class header built successfully");
            
            buildFieldList();
            System.out.println("[ClassPageBuilder] Field list built successfully");
            
            buildMethodList();
            System.out.println("[ClassPageBuilder] Method list built successfully");
            
            super.build(output);
            System.out.println("[ClassPageBuilder] Documentation written to: " + output);
        } catch (IOException e) {
            System.err.println("[ClassPageBuilder] Error building documentation: " + e.getMessage());
            throw e;
        }
    }

}
