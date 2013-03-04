*this is my own personal mirror of Eric Berry's gradle-templates plugin*
You can find the original bzr repo [here](https://launchpad.net/gradle-templates)

I enhanced this version to include a template and a separate plugin for multi project builds.  

You can create a multi project build with 

        gradle createMultiBuildProject

or:
				
	gradle cMBP

This creates the template directory and folder, then you can call tasks

     gradle createJavaSubProject

or

	gradle createGroovySubProject

and the tasks will prepend the line 

    include 'sub-project'

to `settings.gradle`

along with setting up the original template for a java or groovy project as well. 

I was looking for this feature and never really got it, but now I do. :)

I also added a template for my own personal way of handling a multi project files and eliminating all the many `build.gradle` by naming them `some-sub-project.gradle` instead.  And also by checking them in the settings.gradle with 

```groovy
rootProject.children.each { project ->
    project.buildFileName = "\${project.name}.gradle"
    assert project.projectDir.isDirectory()
    assert project.buildFile.isFile()
}
```

So that if a sub-project in the settings.gradle doesn't exist, you are alerted about it, and it also checks to make sure the convention for build file naming is done.  

