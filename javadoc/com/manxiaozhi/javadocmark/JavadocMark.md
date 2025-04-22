# JavadocMark
 Main doclet class that generates markdown documentation.
 
 @author wangzhi

## Fields
### OUTPUT_DIRECTORY
Output directory option name. 
### environment
Doclet environment instance. 
### reporter
Reporter instance for error handling. 
### outputDirectory
Output directory path. 
### options
Options instance. 
## Methods
### init
 Initializes this doclet with the given environment.
 
 @param locale Locale to use for messages
 @param reporter Reporter instance for error handling

#### Parameters
* locale - java.util.Locale
* reporter - jdk.javadoc.doclet.Reporter
#### Returns
void
### getName
 Returns the name of this doclet.
 
 @return The name of this doclet.

#### Returns
java.lang.String
### getSupportedOptions
 Returns the supported options of this doclet.
 
 @return The supported options of this doclet.

#### Returns
java.util.Set<? extends jdk.javadoc.doclet.Doclet.Option>
### getSupportedSourceVersion
 Returns the supported source version.
 
 @return The supported source version.

#### Returns
javax.lang.model.SourceVersion
### run
 Runs this doclet.
 
 @param environment Doclet environment to use.
 @return true if documentation generation succeed, false otherwise.

#### Parameters
* environment - jdk.javadoc.doclet.DocletEnvironment
#### Returns
boolean
