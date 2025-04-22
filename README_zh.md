# JavadocMark

在Cursor等AI驱动的代码编辑器时代，开发者在使用较新的Java依赖包时常常面临挑战。传统的Javadoc文档虽然全面，但对AI模型来说并不总是直接可访问的。这种限制可能导致生成的代码无法完全适配最新的API接口。

**JavadocMark** 正是为解决这一问题而创建。它能生成Markdown格式的Javadoc文档，并直接包含在你的项目中。这种方式使AI模型能够更有效地阅读和理解文档，显著提高使用现代Java库时生成代码的准确性。

**JavadocMark** 是一个自定义的Java Doclet，用于生成Markdown格式的Javadoc文档，可以直接在GitHub上使用。你可以在以下项目中查看**JavadocMark**生成的文档：

* [JavadocMark本身！](https://github.com/Faylixe/marklet/tree/master/javadoc/fr/faylixe/marklet)


## 主要功能

1. 生成包级别的文档（README.md），包含：
   - 包名和描述
   - 类型表格（类、接口、枚举），包含名称、类型和描述
   - 支持内部类，文件名和标题会包含外部类名称

2. 生成类型级别的文档（TypeName.md），包含：
   - 类型名称和完整描述
   - 字段列表和描述
   - 方法列表，包含参数、返回值和异常说明
   - 支持内部类，文件名和标题会包含外部类名称

## 使用方法

在Maven项目的`POM`文件中添加以下配置：

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-javadoc-plugin</artifactId>
    <version>2.9</version>
    <configuration>
        <doclet>com.manxiaozhi.javadocmark.JavadocMark</doclet>
        <docletArtifact>
            <groupId>com.manxiaozhi</groupId>
            <artifactId>javadocmark</artifactId>
            <version>1.0.0</version>
        </docletArtifact>
        <reportOutputDirectory>./</reportOutputDirectory>
        <destDir>./</destDir>
        <additionalparam>-d javadoc/</additionalparam>
        <useStandardDocletOptions>false</useStandardDocletOptions>
    </configuration>
</plugin>
```

这将在项目目录下的`javadoc/`子文件夹中生成文档。

## Java8 doclint问题

如果你使用Java8，可能会遇到doclint验证的问题，特别是在使用markdown块引用语法时。要解决这个问题，只需在`pom.xml`文件中添加以下配置来禁用doclint：

```xml
<properties>
    <additionalparam>-Xdoclint:none</additionalparam>
</properties>
```

## 开发JavadocMark

JavadocMark需要Apache Maven。要构建项目，运行：

```
$ ./mvnw install
```

要为JavadocMark本身生成Markdown文档，运行：

```
$ ./mvnw -P javadocmark-generation javadoc:javadoc
```

## 许可证

JavadocMark使用Apache License, Version 2.0许可证。

## 当前版本特性

当前版本（1.0.1）支持以下特性：

* 生成包级别的Markdown文档
* 生成类型级别的Markdown文档
* 支持内部类的文档生成
* 支持接口和枚举的文档生成
* 支持字段和方法的详细文档
* 支持参数、返回值和异常的文档

如果你发现任何问题，请随时提交Pull Request。
