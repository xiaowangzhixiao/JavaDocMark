package com.manxiaozhi.javadocmark;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic.Kind;

import jdk.javadoc.doclet.Doclet;
import jdk.javadoc.doclet.DocletEnvironment;
import jdk.javadoc.doclet.Reporter;

/**
 * Main doclet class that generates markdown documentation.
 * 
 * @author wangzhi
 */
public final class JavadocMark implements Doclet {

	/** Output directory option name. **/
	private static final String OUTPUT_DIRECTORY = "-d";

	/** Doclet environment instance. **/
	private DocletEnvironment environment;

	/** Reporter instance for error handling. **/
	private Reporter reporter;

	/** Output directory path. **/
	private Path outputDirectory;

	/** Options instance. **/
	private JavadocMarkOptions options;

	/**
	 * Default constructor.
	 */
	public JavadocMark() {
		System.out.println("[JavadocMark] Initializing JavadocMark doclet");
	}

	/**
	 * Initializes this doclet with the given environment.
	 * 
	 * @param locale Locale to use for messages
	 * @param reporter Reporter instance for error handling
	 */
	@Override
	public void init(Locale locale, Reporter reporter) {
		System.out.println("[JavadocMark] Initializing doclet with locale: " + locale);
		this.reporter = reporter;
		this.options = new JavadocMarkOptions(reporter);
	}

	/**
	 * Returns the name of this doclet.
	 * 
	 * @return The name of this doclet.
	 */
	@Override
	public String getName() {
		return "JavadocMark";
	}

	/**
	 * Returns the supported options of this doclet.
	 * 
	 * @return The supported options of this doclet.
	 */
	@Override
	public Set<? extends Option> getSupportedOptions() {
		System.out.println("[JavadocMark] Getting supported options");
		return Set.of(options);
	}

	/**
	 * Returns the supported source version.
	 * 
	 * @return The supported source version.
	 */
	@Override
	public SourceVersion getSupportedSourceVersion() {
		return SourceVersion.latest();
	}

	/**
	 * Runs this doclet.
	 * 
	 * @param environment Doclet environment to use.
	 * @return true if documentation generation succeed, false otherwise.
	 */
	@Override
	public boolean run(final DocletEnvironment environment) {
		System.out.println("[JavadocMark] Starting documentation generation");
		try {
			this.environment = environment;
			this.outputDirectory = options.getOutputDirectory();
			System.out.println("[JavadocMark] Output directory: " + outputDirectory);

			if (!Files.exists(outputDirectory)) {
				System.out.println("[JavadocMark] Creating output directory");
				Files.createDirectories(outputDirectory);
			}

			final Elements elementUtils = environment.getElementUtils();
			final List<PackageElement> packages = new ArrayList<>();
			final List<TypeElement> classes = new ArrayList<>();
			System.out.println("[JavadocMark] Processing included elements");
			
			for (final Element element : environment.getIncludedElements()) {
				if (element.getKind() == ElementKind.CLASS || 
					element.getKind() == ElementKind.INTERFACE || 
					element.getKind() == ElementKind.ENUM) {
					final TypeElement typeElement = (TypeElement) element;
					final PackageElement packageElement = elementUtils.getPackageOf(typeElement);
					if (!packages.contains(packageElement)) {
						System.out.println("[JavadocMark] Found package: " + packageElement.getQualifiedName());
						packages.add(packageElement);
					}
					System.out.println("[JavadocMark] Found class: " + typeElement.getQualifiedName());
					classes.add(typeElement);
				}
			}

			System.out.println("[JavadocMark] Generating documentation for " + packages.size() + " packages");
			for (final PackageElement packageElement : packages) {
				try {
					System.out.println("[JavadocMark] Processing package: " + packageElement.getQualifiedName());
					final PackagePageBuilder builder = new PackagePageBuilder(packageElement, environment);
					final Path packageDirectory = outputDirectory.resolve(packageElement.getQualifiedName().toString().replace('.', '/'));
					if (!Files.exists(packageDirectory)) {
						System.out.println("[JavadocMark] Creating package directory: " + packageDirectory);
						Files.createDirectories(packageDirectory);
					}
					final Path output = packageDirectory.resolve("README.md");
					System.out.println("[JavadocMark] Writing package documentation to: " + output);
					builder.build(output);
				} catch (final IOException e) {
					System.err.println("[JavadocMark] Error processing package " + packageElement.getQualifiedName() + ": " + e.getMessage());
					e.printStackTrace();
					return false;
				}
			}

			System.out.println("[JavadocMark] Generating documentation for " + classes.size() + " classes");
			for (final TypeElement classElement : classes) {
				try {
					System.out.println("[JavadocMark] Processing class: " + classElement.getQualifiedName());
					final PackageElement packageElement = elementUtils.getPackageOf(classElement);
					final Path packageDirectory = outputDirectory.resolve(packageElement.getQualifiedName().toString().replace('.', '/'));
					
					// 处理内部类
					String fileName = classElement.getSimpleName().toString();
					if (classElement.getNestingKind().isNested()) {
						// 获取外部类
						TypeElement enclosingElement = (TypeElement) classElement.getEnclosingElement();
						while (enclosingElement != null && enclosingElement.getNestingKind().isNested()) {
							fileName = enclosingElement.getSimpleName() + "." + fileName;
							enclosingElement = (TypeElement) enclosingElement.getEnclosingElement();
						}
						if (enclosingElement != null) {
							fileName = enclosingElement.getSimpleName() + "." + fileName;
						}
					}
					
					final Path classFile = packageDirectory.resolve(fileName + ".md");
					System.out.println("[JavadocMark] Writing class documentation to: " + classFile);
					
					final ClassPageBuilder builder = new ClassPageBuilder(classElement, environment);
					builder.build(classFile);
				} catch (final IOException e) {
					System.err.println("[JavadocMark] Error processing class " + classElement.getQualifiedName() + ": " + e.getMessage());
					e.printStackTrace();
					return false;
				}
			}

			System.out.println("[JavadocMark] Documentation generation completed successfully");
			return true;
		} catch (final Exception e) {
			System.err.println("[JavadocMark] Fatal error during documentation generation: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
}

