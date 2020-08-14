 <p align="center">
 <img src="https://i.loli.net/2020/05/21/aIDzRnsFWYP6Zfe.png" alt="OnlyViewer" width="100">
 </p>
<h1 align="center">OnlyViewer</h1>

> 🎈 Only when you view the images more delightfully shall we be happier.

[English Version README](README_en.md)

# 简介

>码云仓库地址：[https://gitee.com/kevin996/OnlyViewer](https://gitee.com/kevin996/OnlyViewer)
>
>Github仓库地址: [https://github.com/greyovo/onlyviewer](https://github.com/greyovo/onlyviewer)

本项目为学校的课程设计作业：实现一个图片查看、管理应用程序，包含一些基本图片管理功能，如：

* 复制 / 剪切 和 粘贴
* 删除
* 重命名
* 查看图片的属性
* 多选

双击缩略图，能实现基本的展示功能，如：

* 放大
* 缩小
* 切换图片
* 幻灯片展示

此外，在实现基本功能的情况下加入一些扩展功能，如：

* OCR文本识别
* 压缩图片
* 历史记录
* 图片拼接
* 排序
* 搜索等

# 运行界面

我们借助JFoenix[JFoenix](http://www.jfoenix.com)实现了Material Design风格的界面。

<p align="center">
 <img src="https://i.loli.net/2020/05/21/UqpAJzVoPY2dGbf.png" alt="The welcome page" width="800px">
 </p>
 
<p align="center">(启动页)</p> 

<p align="center">
 <img src="https://i.loli.net/2020/05/21/VxNOIAuWoCTKy87.png" alt="TreeView & Thumbnails" width="800px">
 </p>
 
<p align="center">(目录树 & 缩略图)</p> 

<p align="center">
 <img src="https://i.loli.net/2020/05/21/pxSNr76VA5Ybtq1.png" alt="Dialog" width="800px">
 </p>

<p align="center">(对话框样式)</p> 

<p align="center">
 <img src="https://i.loli.net/2020/05/21/LdNrXgZOp7s1Kni.png" alt="Display Window" width="800px">
 </p>

<p align="center">(单独预览窗口)</p> 

# 使用的库

本项目基于Java 8（JavaFX 2.0），使用Maven进行项目管理，包含以下依赖：

* JFoenix
* Lombok
* Thumbnailator
* Baidu AI

> 注意：使用IDEA或Eclipse时，请安装IDE对应的**Lombok插件**，避免编辑器误报错误。

# FAQ

推荐使用IDEA打开本项目。若更新图片资源文件后重新编译出错的问题，请尝试执行Maven的clean命令。

### Maven已经全部下好依赖了，但为什么还是出现许多错误提示？（如某getter、setter方法不存在等）
注意，在使用IDEA或Eclipse时，请安装IDE对应的**Lombok插件**，避免编辑器误报错误。

### 文字识别不能使用？
文字识别调用的是百度AI的接口，需要您自行前往百度AI -> 控制台 -> 文字识别 -> 添加应用获取API_KEY与SECRET_KEY, 更新src/onlyviewer/display/java/Ocr.java
[百度AI](https://ai.baidu.com/)

### 还有问题？
请提issue。

# 优化方向

尽管在提交项目评分之时我们已尽力做到最好，但因为时间紧迫加上水平有限，缺陷总是在所难免的。这是一些已知的可优化的方向，我们不一定会实现，仅作记录。

- [ ] 多线程加载缩略图以提高运行速度
- [ ] 优化内存占用
- [ ] 多语言支持
- [ ] 记忆上次浏览的文件夹
- [ ] 更多...


# 特别感谢

* [aleksandarstojkovski](https://github.com/aleksandarstojkovski) / [PictureX-Image-Processor](https://github.com/aleksandarstojkovski/PictureX-Image-Processor)
* [coobird](https://github.com/coobird) / [thumbnailator](https://github.com/coobird/thumbnailator)
* [jfoenixadmin](https://github.com/jfoenixadmin) / [JFoenix](https://github.com/jfoenixadmin/JFoenix)

# 关于作者

由 [Kevin](https://github.com/Kevin996233), [Grey](https://github.com/greyovo) 和 [tudou daren](https://github.com/tudoudaren233) 共同协作完成。

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
