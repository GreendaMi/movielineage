# movielineage
一款在线视频播放app，使用了Vitamio。

![9点片场](https://github.com/GreendaMi/movielineage/blob/master/mm2.gif)

&ensp;&ensp;使用了Vitamio作为视频播放框架，实现了基本的视频**全屏**播放，进度调节。

&ensp;&ensp;除了这些还有其他的小亮点。比如侧边栏的[水波动画](http://blog.csdn.net/jdsjlzx/article/details/44601239)
，实时的图片高斯模糊，ViewPager+Fragment实现的主界面，图片展示使用RecyclerView，图片缓存Gilde，网络请求OKHttp，逻辑处理RxJava，爬虫解析Jsoup。里面所有数据均爬自[新片场](http://www.xinpianchang.com/)。

&ensp;&ensp;需要引用Vitamio的项目作为依赖，[Vitamio下载]。(http://wscdn.miaopai.com/download/vitamio20160930.zip)

&ensp;&ensp;播放界面封装成了一个Activity，只需要传入视频的url，方便各位拿去使用。

![影片信息界面](https://github.com/GreendaMi/movielineage/blob/master/mm5.gif?raw=true)

&ensp;&ensp;重新修改了影片信息界面，使用了CoordinatorLayout的nestedscrolling特性，实现了状态栏的颜色渐变与滚动。添加了新控件FloatingActionButton。添加转场的过度效果。
