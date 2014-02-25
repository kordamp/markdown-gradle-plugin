Markdown Gradle Plugin
-------------------------

[![Travis Build Status](https://travis-ci.org/aalmiray/markdown-gradle-plugin.png?branch=master)](https://travis-ci.org/aalmiray/markdown-gradle-plugin)
[![Coverage Status](https://coveralls.io/repos/aalmiray/markdown-gradle-plugin/badge.png)](https://coveralls.io/r/aalmiray/markdown-gradle-plugin)
[![Semantic Versioning](http://img.shields.io/:semver-0.1.0-blue.svg)](http://semver.org)

[![Bintray](https://api.bintray.com/packages/aalmiray/kordamp/markdown-gradle-plugin/images/download.png)](https://bintray.com/aalmiray/kordamp/markdown-gradle-plugin)

This plugin provides a facility for converting markdown into HTML, as well as
converting HTML back into markdown. It is based on the [grails-markdown][]
plugin by Ted Naleid.

See [Daring Fireball][] for syntax basics.

This plugin makes use of the [Pegdown][] and [Remark][] libraries.

Installation
------------

Use the following snippet

    buildscript {
        repositories {
            jcenter()
            mavenRepo name: 'Bintray', url: 'http://dl.bintray.com/content/aalmiray/kordamp'
        }
        dependencies {
            classpath 'org.kordamp:markdown-gradle-plugin:0.1'
        }
    }
    apply plugin: 'markdown'


Usage
-----

The plugin adds 2 tasks named `markdownToHtml` and `htmlToMarkdown`. These task expose
three properties as part of their configuration

#### markdownToHtml

 * source - where the markdown sources are. Type: File. Default: `src/markdown`.
 * outputDir - where generated html go. Type: File. Default: `$buildDir/gen-html`.
 * configuration - a Map with further config tweaks. Explained in the next section
 
Sources may have any of the following extensions in order to be discovered

 * .md
 * .markdown

Non markdown files will be copied "as is" to `outputDir`.

#### htmlToMarkdown

 * sourceDir - where the html sources are. Type: File. Default: `src/html`.
 * outputDir - where generated markdown go. Type: File. Default: `$buildDir/gen-markdown`.
 * configuration - a Map with further config tweaks. Explained in the next section
 
Sources may have any of the following extensions in order to be discovered

 * .html

Non html files will be copied "as is" to `outputDir`.

## Configuration ##

The default configuration is almost 100% pure Markdown, with one caveat:

> NOTE: The Markdown engine does not allow in-word emphasis.
>
> This means that when you write `an_emphasized_word`, you don't get <code>an<em>emphasized</em>word</code>.
> You just get `an_emphasized_word`. This is true no matter the character used
> (`_` or `*`), or for italics or bold.

#### Hardwraps ####

    markdown.hardwraps = true        // Configuration
    [hardwraps: true]                // Custom Map

Markdown makes simple hardwraps a little difficult, requiring the user to write
two spaces at the end of a line to get a linebreak. This is convenient when
writing in a terminal, but inconvenient if your editor handles soft-wraps internally.

Enabling hardwraps means that all linebreaks are kept.

#### Auto Links ####

    markdown.autoLinks = true        // Configuration
    [autoLinks: true]                // Custom Map

Auto Linking enables conversion of HTTP and HTTPS urls into links without explicit
link generation.

Example Markdown:

    http://gradle.org/

Example HTML:

    <a href="http://gradle.org/">http://gradle.org/</a>

#### Abbreviations ####

    markdown.abbreviations = true    // Configuration
    [abbreviations: true]            // Custom Map

Enables abbreviations are in the [Markdown Extra][] style. These allow the Markdown
output to generate `<abbr>` tags.

Example Markdown:

    This is HTML

    *[HTML]: Hyper-Text Markup Language

Example HTML:

    This is <abbr title="Hyper-Text Markup Language">HTML</abbr>

#### Definition Lists ####

    markdown.definitionLists = true  // Configuration
    [definitionLists: true]          // Custom Map

Enables `<dl>` lists in the [Markdown Extra][] style.

Example Markdown:

    Gradle
    :   Gradle is build automation evolved.

Example HTML:

    <dl>
        <dt>Gradle</dt>
        <dd>Gradle is build automation evolved.</dd>
    </dl>

#### Smart Quotes, Smart Punctation ####

    markdown.smartQuotes = true      // Configuration
    [smartQuotes: true]              // Custom Map
    markdown.smartPunctuation = true // Configuration
    [smartPunctuation: true]         // Custom Map
    // or, for both use
    markdown.smart = true            // Configuration
    [smart: true]                    // Custom Map

Enables conversion of simple quotes and punctuation into HTML entities and back
again, such as converting `"Foo"` into `“Foo”`, or `---` into `—`.

#### Fenced Code Blocks ####

    markdown.fencedCodeBlocks = true // Configuration
    [fencedCodeBlocks: true]         // Custom Map

Allows the use of several tildes (`~`) to delineate code blocks, instead of
forcing the users to indent each line four spaces.

> Note: If enabled, all code blocks will use fences when converting HTML back
> into Markdown.

#### Tables ####

    markdown.tables = true           // Configuration
    [tables: true]                   // Custom Map

If tables are allowed, you can create tables using [Markdown Extra][] or
[Multimarkdown][] syntax. This also converts tables from HTML *back* into clean,
easy-to-read plain text tables.

An example in Markdown:

    |              |          Grouping           ||
    | First Header | Second Header | Third Header |
    |:------------ |:-------------:| ------------:|
    | Content      |         *Long Cell*         ||
    | Content      |   **Cell**    |         Cell |
    | New Section  |     More      |         Data |
    | And more     |          And more           ||

#### All ####

The `all` option easily enables these items:

 *  Hardwraps
 *  Auto Links
 *  Abbreviations
 *  Definition Lists
 *  Smart Quotes
 *  Smart Punctuation
 *  Fenced Code Blocks
 *  Tables

#### Remove HTML ####

    markdown.removeHtml = true       // Configuration
    [removeHtml: true]               // Custom Map

With this option enabled, all raw HTML will be removed when converting Markdown
to HTML.

#### Remove Tables ####

    markdown.removeTables = true     // Configuration
    [removeTables: true]             // Custom Map

Removes tables when converting HTML to Markdown, instead of leaving them as-is.

#### Base URI ####

    markdown.baseUri = 'http://example.com'

You can override the default base URI (which is determined by your configuration).
The base URI is used when converting relative links.

Setting it to `false` will simply remove relative links.

#### Customize Pegdown ####

    markdown.customizePegdown = { int extensions -> ... }

Allows for customization of the Pegdown extensions before creating a
`PegdownProcessor` using a closure. This closure will be called at the time the
`PegdownProcessor` is first needed, not necessarily at startup.

#### Customize Remark ####

    markdown.customizeRemark = { com.overzealous.remark.Options options -> ... }

Allows for customization of the Remark `Options` before creating a `Remark` using
a closure. This closure will be called at the time the `Remark` is first needed,
not necessarily at startup.

History
-------

### 0.1

 * First release.

[![Bitdeli Badge](https://d2weczhvl823v0.cloudfront.net/aalmiray/markdown-gradle-plugin/trend.png)](https://bitdeli.com/free "Bitdeli Badge")

[grails-markdown]: http://grails.org/plugin/markdown
[Daring Fireball]: http://daringfireball.net/projects/markdown/basics
[Pegdown]: http://pegdown.org
[Remark]: http://remark.overzealous.com/manual/index.html
[Markdown Extra]: http://michelf.com/projects/php-markdown/extra/
[Multimarkdown]: http://fletcher.github.com/peg-multimarkdown/#tables
