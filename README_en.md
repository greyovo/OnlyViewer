 <p align="center">
 <img src="https://i.loli.net/2020/05/21/aIDzRnsFWYP6Zfe.png" alt="OnlyViewer" width="100">
 </p>
<h1 align="center">OnlyViewer</h1>

> 🎈 Only when you view the images more delightfully shall we be happier.

[中文版本README](README.md)

# Introduction

>Gitee: [https://gitee.com/kevin996/OnlyViewer](https://gitee.com/kevin996/OnlyViewer)
>
>Github: [https://github.com/greyovo/onlyviewer](https://github.com/greyovo/onlyviewer)

This is an end-project of our Java course: to realize a software (system) with the function of viewing pictures and management. It's a very powerful and useful tool that you can easily handle pictures in your personal computer, enjoy your time with your photos and enjoy our app 😘. If you have any ideas or advice, just open an issue or create a pull request.

Here are some basic functions:

* Copy / Cut and paste
* Delete
* Rename
* Check out the attributes
* Multiple Selection

By double-clicking the thumbnail, it will show the picture in a separate window with some features below:

* Zoom in / Zoom out
* Switch pictures
* Slide show

We also maintain some creative features in this app for you :

* OCR text recognition
* Picture compression
* History records
* Picture stitching
* Sorting
* More...

# Appearance

We use [JFoenix](http://www.jfoenix.com) to compose a wonderful user interface with Material Design.

<p align="center">
 <img src="https://i.loli.net/2020/05/21/UqpAJzVoPY2dGbf.png" alt="The welcome page" width="800px">
 </p>
 
<p align="center">(The welcome page)</p> 

<p align="center">
 <img src="https://i.loli.net/2020/05/21/VxNOIAuWoCTKy87.png" alt="TreeView & Thumbnails" width="800px">
 </p>
 
<p align="center">(Treeview & Thumbnails)</p> 

<p align="center">
 <img src="https://i.loli.net/2020/05/21/pxSNr76VA5Ybtq1.png" alt="Dialog" width="800px">
 </p>

<p align="center">(Dialog)</p> 

<p align="center">
 <img src="https://i.loli.net/2020/05/21/LdNrXgZOp7s1Kni.png" alt="Display Window" width="800px">
 </p>
 
<p align="center">(Display window)</p> 

# Platform and Libraries

This project is based on Java 8 (JavaFX 2.0). We use Maven to manage our external libraries, such as: 

* JFoenix
* Lombok
* Thumbnailator
* Baidu AI

> Note: Remember to install the **Lombok Plugin** when using IDEA or Eclipse, otherwise the editor would come up with false alarms.

# FAQ

We recommend you to open this project in IDEA.

### I have downloaded all the dependencies, why still so many errors? (say some getter or setter doesn't exist)
Install the **Lombok Plugin** in your IDEA or Eclipse, otherwise the editor would come up with false alarms.

### Can not use OCR?
OCR need support from ai.baidu, please go to the website -> click into the console -> OCR -> build a new application to get API_KEY and SECRET_KEY, then update src/onlyviewer/display/java/Ocr.java
[百度AI](https://ai.baidu.com/)

### More questions?
Please leave an issue.

# Optimization

The list below shows how we can improve this app. Notice that we may not accomplish them.

- [ ] Use multi-thread to improve loading rate of thumbnail
- [ ] Optimize memory usuage
- [ ] Multi-language support
- [ ] Remember where the user last visited
- [ ] etc...

# Special Thanks

* [aleksandarstojkovski](https://github.com/aleksandarstojkovski) / [PictureX-Image-Processor](https://github.com/aleksandarstojkovski/PictureX-Image-Processor)
* [coobird](https://github.com/coobird) / [thumbnailator](https://github.com/coobird/thumbnailator)
* [jfoenixadmin](https://github.com/jfoenixadmin) / [JFoenix](https://github.com/jfoenixadmin/JFoenix)

# Authors

Authored and maintained by [Kevin](https://github.com/Kevin996233), [Grey](https://github.com/greyovo) and [tudou daren](https://github.com/tudoudaren233). 

# Licence

```plain
Copyright [2020] [OnlyViewer Maintainer]
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and 
limitations under the License.
```
