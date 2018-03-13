
var $ = function(id){
    return document.getElementById(id);
}

$('plusNum').onclick = function(e){
    e = window.event || e;
    o = e.srcElement || e.target;
    var num = $('allNum').textContent;
    if(num > 0){
        num --;
        $('allNum').innerHTML = num;
    }else{
        alert("购买数量不能小于0");
    }
};

$('addNum').onclick = function(e){
    e = window.event || e;
    o = e.srcElement || e.target;
    var num = $('allNum').textContent;
    num ++;
    $('allNum').innerHTML = num;
};

var loading = new Loading();
var layer = new Layer();


$('add').onclick = function(e){

    var num = $('allNum').textContent;
    if(num == 0)
        alert("购买数量不能为0");
    else{    var ele = e.target;
        var id = ele && ele.dataset.id;
        id = parseInt(id);
        var title = ele && ele.dataset.title;
        var price = ele && ele.dataset.price;
        price = parseFloat(price);
        var num = $('allNum').innerHTML;
        num = parseInt(num);
        var productDetail = {'id':id,'price':price,'title':title,'num':num};

        e === window.event || e;
        layer.reset({
            content:'确定要加入购物车吗？',
            onconfirm:function(){
                layer.hide();
                loading.show();

                var xhr = new XMLHttpRequest();
                var data = JSON.stringify(productDetail);
                console.log(data);

                xhr.onreadystatechange = function(){
                    if(xhr.readyState == 4){
                        var status = xhr.status;
                        if(status >= 200 && status < 300 || status == 304){
                            var json = JSON.parse(xhr.responseText);
                            if(json && json.code == 200){
                                loading.result('加入购物车成功');
                            }else{
                                alert(json.message);
                            }
                        }else{
                            var json = JSON.parse(xhr.responseText);
                            loading.result(json.message||'加入购物车失败');
                        }
                    }
                };
                xhr.open('post','/api/addCartContent');
                xhr.setRequestHeader('Content-Type','application/json');
                xhr.send(data);
            }.bind(this)
        }).show();
    }
    return;
};



