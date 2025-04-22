# JavadocMarkOptions
 Class that contains JavadocMark doclet options.
 
 @author wangzhi

## Fields
### OUTPUT_DIRECTORY
Output directory option name. 
### outputDirectory
Output directory path. 
### reporter
Reporter instance for error handling. 
## Methods
### getOutputDirectory
 Getter for the output directory path.
 
 @return Output directory path.

#### Returns
java.nio.file.Path
### getArgumentCount
#### Returns
int
### getDescription
#### Returns
java.lang.String
### getKind
#### Returns
jdk.javadoc.doclet.Doclet.Option.Kind
### getNames
#### Returns
java.util.List<java.lang.String>
### getParameters
#### Returns
java.lang.String
### process
#### Parameters
* option - java.lang.String
* arguments - java.util.List<java.lang.String>
#### Returns
boolean
