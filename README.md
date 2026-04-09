# numworx-dwo-author

This repository contains the authoring components of the numworx project.

## Introduction

The purpose of this software is to create configurable editors for the numworx platform.
These editors are loaded on demand by the Numworx Author application to edit and configure the activities and widgets of the platform.
 * see [https://github.com/wimvvelt/numworx-author](https://github.com/wimvvelt/numworx-author) for the application
 * see [https://github.com/UtrechtUniversity/numworx-dwo-project](https://github.com/UtrechtUniversity/numworx-dwo-project) for the platform

## Prerequisites

This project depends on the artifacts build by numworx-dwo-project project. To build the software, use maven running under a Java-11 JDK.

## Contents 

### Folder structure

Each folder contains a maven submodule.

### File formats 

This is a big Maven project, written in Java 8 and 11. See the individual pom.xml files.

## Usage
 
All artifacts go to a static website. The numworx-dwo-resources project builds such an website as a docker container.
Configure the numworx author application with the location of that static website. The default is https://cdn.dwo.nl/jars/ using cloudfront for caching.

## License

This work is licensed under the GNU GPL-3 License.
Copyright 2006, Utrecht University, all rights reserved

## Contact 

[Wim van Velthoven](mailto:w.p.g.vanvelthoven@uu.nl)
