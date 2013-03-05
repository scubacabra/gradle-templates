*this is my own personal mirror of Eric Berry's gradle-templates plugin*
You can find the original bzr repo [here](https://launchpad.net/gradle-templates)

Installation
==========
```groovy
buildscript { 
  repositories { 
    ivy { 
      url 'http://dl.bintray.com/content/djmijares/gradle-plugins'
    }
  }

  dependencies {
    classpath 'gradle-templates:templates:1.3.2'
  }
}

apply plugin: 'templates'
```

Enhancements
==========

I enhanced this version to include a template and a separate plugin for multi project builds.  I also wanted to make my own builder and separate tasks into their own classes, so I kind of rewrote everything to make re-use code a little easier.

Multi Build Project
----------
There is now support for creating a multi build project!

You can create a multi project build with 

     gradle createMultiBuildProject

or:
				
     gradle cMBP

This creates the template directory and root folder for a multi build project.  

Also, the template `build.gradle` generated is referencing another plugin for the `rootProject` **ONLY**.  That is the plugin `multi-project-templates`.  This plugin allows you to generate the Sub Projects with the commands below.

Creating Sub Project
----------

You can call tasks to create sub projects with

     gradle createJavaSubProject

or

     gradle createGroovySubProject

and the tasks will prepend the line 

    include 'sub-project-name'

to `settings.gradle`

along with setting up the original template for a java or groovy project as well. 

I was looking for this feature and never really got it, but now I do. :)

Multi Build Project Conventions
----------

I also added a template for my own personal way of handling a multi project files and eliminating all the many `build.gradle` by naming them `some-sub-project.gradle` instead.  And also by checking them in the settings.gradle with 

```groovy
rootProject.children.each { project ->
    project.buildFileName = "\${project.name}.gradle"
    assert project.projectDir.isDirectory()
    assert project.buildFile.isFile()
}
```

So that if a sub-project in the settings.gradle doesn't exist, you are alerted about it, and it also checks to make sure the convention for build file naming is done.  

Support for test templates
==========

when creating a Java class or a groovy class with 

     gradle create[Java|Groovy]Class

you are now prompted if you would like to create a Test with the choices being:

1. Spock Test
2. JUnit Test

This is also something I thought would be cool, that I have used with grails, but not Gradle -- that changes.

Future Changes
==========

* Code Clean up
* Generate Test Class Tasks
* Generate Examples Folders for Gradle Plugins
* Add gradle wrapper to gradle Plugins Tasks
