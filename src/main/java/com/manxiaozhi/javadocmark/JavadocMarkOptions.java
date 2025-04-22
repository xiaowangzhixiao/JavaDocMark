package com.manxiaozhi.javadocmark;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import jdk.javadoc.doclet.Doclet;
import jdk.javadoc.doclet.Reporter;

/**
 * Class that contains JavadocMark doclet options.
 * 
 * @author wangzhi
 */
public final class JavadocMarkOptions implements Doclet.Option {

	/** Output directory option name. **/
	private static final String OUTPUT_DIRECTORY = "-d";

	/** Output directory path. **/
	private Path outputDirectory;

	/** Reporter instance for error handling. **/
	private final Reporter reporter;

	/**
	 * Default constructor.
	 * 
	 * @param reporter Reporter instance for error handling.
	 */
	public JavadocMarkOptions(final Reporter reporter) {
		this.reporter = reporter;
		this.outputDirectory = Paths.get(".");
	}

	/**
	 * Getter for the output directory path.
	 * 
	 * @return Output directory path.
	 */
	public Path getOutputDirectory() {
		return outputDirectory;
	}

	@Override
	public int getArgumentCount() {
		return 1;
	}

	@Override
	public String getDescription() {
		return "Output directory for generated markdown files";
	}

	@Override
	public Kind getKind() {
		return Kind.STANDARD;
	}

	@Override
	public List<String> getNames() {
		List<String> names = new ArrayList<>();
		names.add(OUTPUT_DIRECTORY);
		return names;
	}

	@Override
	public String getParameters() {
		return "directory";
	}

	@Override
	public boolean process(String option, List<String> arguments) {
		if (OUTPUT_DIRECTORY.equals(option) && !arguments.isEmpty()) {
			System.out.println("[JavadocMarkOptions] Processing output directory: " + arguments.get(0));
			outputDirectory = Paths.get(arguments.get(0));
			System.out.println("[JavadocMarkOptions] Output directory set to: " + outputDirectory);
			return true;
		}
		return false;
	}

}
