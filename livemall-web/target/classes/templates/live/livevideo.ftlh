<!DOCTYPE html>
<html>
    <head>
        <link href="http://vjs.zencdn.net/5.5.3/video-js.css" rel="stylesheet">
        <script src="http://vjs.zencdn.net/5.5.3/video.js"></script>
    </head>


    <style>
        *{margin: 0;padding: 0}
        html,body{width: 100%;height: 100%}
    </style>
    <body>
        <div  style="width: 100%;height: 100%">
            <video id="my-video" class="video-js" controls preload="auto" poster="" data-setup="{}" style="object-fit: fill;width: 100%;height: 100%;">
                <source src="${replayurl!}" type='rtmp/vlc'>
            </video>
        </div>
    </body>


</html>