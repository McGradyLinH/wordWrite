/**
 * Created by ROOT on 2020/7/28.
 */

/**
 * @param source    图片源的表单
 * @param show      要展示图片的div
 * @param async     图片处理完成后要进行的操作，如ajax上传。等信息
 */

const async = {};
//处理图片后进行的操作，如上传
async.do = function () {
    let imageData = $('#show>img')[0].src;//获取base64格式的图片
    imageData = imageData.replace(/data:image\/jpeg;base64,/, '');//去掉"data:image/jpeg;base64,"
    let titleCode = $('#titleCode').val();//获取base64格式的图片
    let titleName = $('#titleName').val();//获取base64格式的图片
    //上传图片
    $.post('/uploadEssay', {imageString: imageData, titleCode: titleCode, titleName: titleName}, function (data) {
        const a = document.createElement('a');
        a.setAttribute('href', '/stuIndex');
        // a.setAttribute('target', '_blank');
        a.setAttribute('id', 'startTelMedicine');
        // 防止反复添加
        if(document.getElementById('startTelMedicine')) {
            document.body.removeChild(document.getElementById('startTelMedicine'));
        }
        document.body.appendChild(a);
        a.click();
    })
};
let source = $('#myFile1');
let show = $('#show');

$(function () {
    $("#myFile1").change(function () {
        //给img标检的src赋值
        // document.getElementById("tank").src = URL.createObjectURL($(this)[0].files[0]);
        imageProcess(source, show, 1);
    });
    $("#uploadEssay").click(function () {
        async.do();
    })

    $("#chooseTitle").click(function () {
        if ($("#chooseTitle").text() === '选择题目') {
            $.ajax({
                url: '/checkSurplus',
                type: 'get',
                success: function (data) {
                    if (data=='success'){
                        $(".modal").modal();
                    }else {
                        bootbox.alert("没有更多的修改次数！");
                    }
                }
            })
        } else {
            $("#selectTitle").html('');
            $("#titleName").val('');
            $("#titleCode").val('');
            $("#chooseTitle").text('选择题目');
            $("#uploadArea").css('display', 'none');
        }
    });
})
// container.style = "width:200px;height:200px;border:3px solid;display: none;";
// tank.style = "width:200px;height:200px;position:relative;top:0px;left:0px;"
document.body.onkeydown = function () {
    // let current = 0;
    const code = event.keyCode;
    if (code == 37) {
        // current = 180;
        // tank.style.transform = 'rotate(' + current + 'deg)';
        imageProcess(source, show, 3);
    }
    if (code == 38) {
        // current = 270;
        // tank.style.transform = 'rotate(' + current + 'deg)';
        imageProcess(source, show, 8);
    }
    if (code == 39) {
        // current = 0;
        // tank.style.transform = 'rotate(' + current + 'deg)';
        imageProcess(source, show, 1);
    }
    if (code == 40) {
        // current = 90;
        // tank.style.transform = 'rotate(' + current + 'deg)';
        imageProcess(source, show, 6);
    }
}

function imageProcess(source, show, rotateDirection) {
    // source.change(function () {
    //监听图片源表单的改变事件
    const files = $("input[name='essayFile']").prop("files");
    if (files.length) {
        const isImage = checkFile(files);
        if (!isImage) {
            show.html("请确保文件为图像类型");
        } else {
            const reader = new FileReader();
            reader.onload = function (e) {
                const imageSize = e.total;//图片大小
                const image = new Image();
                image.src = e.target.result;
                image.onload = function () {
                    // 旋转图片
                    let newImage = rotateImage(image, rotateDirection);
                    //压缩图片
                    newImage = judgeCompress(newImage, imageSize);
                    newImage.setAttribute('width', '100%');
                    show.html(newImage);
                    // async.do();
                }
            }
            reader.readAsDataURL(isImage);
        }
    }
    // })
}

/**
 * 检查文件是否为图像类型
 * @param files         FileList
 * @returns file        File
 */
function checkFile(files) {
    var file = files[0];
    //使用正则表达式匹配判断
    if (!/image\/\w+/.test(file.type)) {
        return false;
    }
    return file;
}

/**
 * 判断图片是否需要压缩
 * @param image          HTMLImageElement
 * @param imageSize      int
 * @returns {*}          HTMLImageElement
 */
function judgeCompress(image, imageSize) {
    //判断图片是否大于3m
    const threshold = 4135728;//阈值,可根据实际情况调整
    console.log('imageSize:' + imageSize)
    if (imageSize > threshold) {
        const imageData = compress(image);
        const newImage = new Image();
        newImage.src = imageData
        return newImage;
    } else {
        return image;
    }
}

/**
 *压缩图片
 * @param image         HTMLImageElement
 * @returns {string}    base64格式图像
 */
function compress(image) {
    const canvas = document.createElement('canvas');
    const ctx = canvas.getContext('2d');
    const imageLength = image.src.length;
    const width = image.width;
    const height = image.height;

    canvas.width = width;
    canvas.height = height;

    ctx.drawImage(image, 0, 0, width, height);

    //压缩操作
    const quality = 0.1;//图片质量  范围：0<quality<=1 根据实际需求调正
    const imageData = canvas.toDataURL("image/jpeg", quality);

    console.log("压缩前：" + imageLength);
    console.log("压缩后：" + imageData.length);
    console.log("压缩率：" + ~~(100 * (imageLength - imageData.length) / imageLength) + "%");
    return imageData;
}


/**
 * 旋转图片
 * @param image         HTMLImageElement
 * @returns newImage    HTMLImageElement
 */
function rotateImage(image, rotateDirection) {
    const width = image.width;
    const height = image.height;
    const canvas = document.createElement("canvas");
    const ctx = canvas.getContext('2d');
    let newImage = new Image();
    //旋转图片操作
    EXIF.getData(image, function () {
            // var orientation = EXIF.getTag(this,'Orientation');
            let orientation = rotateDirection;//测试数据
            console.log('orientation:' + orientation);
            switch (orientation) {
                //正常状态
                case 1:
                    console.log('旋转0°');
                    newImage = image;
                    break;
                //旋转90度
                case 6:
                    console.log('旋转90°');
                    canvas.height = width;
                    canvas.width = height;
                    ctx.rotate(Math.PI / 2);
                    ctx.translate(0, -height);

                    ctx.drawImage(image, 0, 0)
                    imageDate = canvas.toDataURL('Image/jpeg', 1)
                    newImage.src = imageDate;
                    break;
                //旋转180°
                case 3:
                    console.log('旋转180°');
                    canvas.height = height;
                    canvas.width = width;
                    ctx.rotate(Math.PI);
                    ctx.translate(-width, -height);

                    ctx.drawImage(image, 0, 0)
                    imageDate = canvas.toDataURL('Image/jpeg', 1)
                    newImage.src = imageDate;
                    break;
                //旋转270°
                case 8:
                    console.log('旋转270°');
                    canvas.height = width;
                    canvas.width = height;
                    ctx.rotate(-Math.PI / 2);
                    ctx.translate(-width, 0);

                    ctx.drawImage(image, 0, 0)
                    imageDate = canvas.toDataURL('Image/jpeg', 1)
                    newImage.src = imageDate;
                    break;
                //undefined时不旋转
                case undefined:
                    console.log('undefined  不旋转');
                    newImage = image;
                    break;
            }
        }
    );
    return newImage;
}