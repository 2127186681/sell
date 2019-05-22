<html>
    <#include "../common/hander.ftl">
<body>

<div id="wrapper" class="toggled">

    <#--边栏sidebar-->
    <#include "../common/nav.ftl">

    <#--一些主要内容content-->
     <div id="page-content-wrapper">
        <div class="container-fluid">
            <div class="row clearfix">
                <div class="col-md-12 column">
                    <table class="table table-striped table-condensed">
                        <thead>
                        <tr>
                            <th>订单ID</th>
                            <th>姓名</th>
                            <th>手机号</th>
                            <th>地址</th>
                            <th>金额</th>
                            <th>订单状态</th>
                            <th>支付状态</th>
                            <th>创建时间</th>
                            <th colspan="2">操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list orderDtoPage.content as orderDTO>
                            <tr>
                            <td>${orderDTO.orderId}</td>
                            <td>${orderDTO.buyerName}</td>
                            <td>${orderDTO.buyerPhone}</td>
                            <td>${orderDTO.buyerAddress}</td>
                            <td>${orderDTO.orderAmount}</td>
                            <td>${orderDTO.getOrderStatusEnum().message}</td>
                            <td>${orderDTO.getPayStatusEnum().getMessage()}</td>
                            <td>${orderDTO.createTime}</td>
                            <td><a href="/sell/seller/order/detail?orderId=${orderDTO.getOrderId()}">详情</a></td>
                            <#if orderDTO.getOrderStatus() == 0>
                                <td><a href="/sell/seller/order/cancel?orderId=${orderDTO.getOrderId()}">取消</a></td>
                            </#if>

                            </tr>
                        </#list>
                        </tbody>
                    </table>
                </div>
                <#--分页-->
                <div class="col-md-12 column">
                    <ul class="pagination pull-right">
                        <#if currentPage lte 1 >  <#--判断 当前页等于小于1 不可点击-->
                            <li class="disabled"><a href="#">上一页</a></li>
                        <#else>
                            <li><a href="/sell/seller/order/List?page=${currentPage - 1}&size=${size}">上一页</a></li>
                        </#if>

                        <#--动态展示页数-->
                        <#list 1..orderDtoPage.getTotalPages() as index>

                            <#if orderDtoPage.getTotalPages() gt 10>


                                <#if index gt 5 && index lt orderDtoPage.getTotalPages() - 5>
                                    <li class="disabled"> <a href="#">...</a> </li>

                                <#else>
                                    <#if currentPage == index> <#--判断 如果是当前页 不可点击-->
                                        <li class="disabled"> <a href="#">${index}</a> </li>
                                    <#else> <#--不是当前页 可点击-->
                                        <li> <a href="/sell/seller/order/List?page=${index}&size=${size}">${index}</a> </li>
                                    </#if>
                                </#if>

                            </#if>





                        </#list>

                        <#if  currentPage gte orderDtoPage.getTotalPages()>
                            <li class="disabled"><a href="#">下一页</a></li>
                        <#else>
                            <li><a href="/sell/seller/order/List?page=${currentPage + 1}&size=${size}">下一页</a></li>
                        </#if>

                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>


<#--webscoket消息弹窗-->
<div class="modal fade" id="Mymodal" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                <h4 class="modal-title" id="myModalLabel">
                    提醒
                </h4>
            </div>
            <div class="modal-body">
                您有新的订单！
            </div>
            <div class="modal-footer">
                <button onclick="javascript:document.getElementById('notice').pause()" type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" onclick="location.reload()" class="btn btn-primary">查看新订单</button>
            </div>
        </div>

    </div>

</div>
<#--播放音乐-->
<audio id="notice" loop="loop" >
    <source src="/sell/mp3/song.mp3" type="audio/mpeg" >
</audio>


<script src="https://cdn.bootcss.com/jquery/1.12.4/jquery.min.js"></script>
<script src="https://cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<script>   /*websocket通信前端代码*/
    var websocket = null;
    if('WebSocket' in window){
        websocket = new WebSocket('ws://lysell.natapp1.cc/sell/webSocket');
    }else {
        alert("该浏览器不支持websocket！");
    }

    websocket.onopen = function (event) {
        console.log('建立连接');
    }

    websocket.onclose = function (event) {
        console.log('关闭连接');
    }

    websocket.onmessage = function (event) { //接受后端发来的消息
        console.log('收到消息:'+event.data);
        //收到消息 弹窗提醒
        $('#Mymodal').modal('show');
        //播放音乐
        document.getElementById('notice').play();
    }

    websocket.onerror = function () {
        alert('websocket痛惜发生错误');
    }

    window.onbeforeunload = function () {
        websocket.close();
    }

</script>

</body>
</html>

<#--
<#list orderDtoPage.content as orderDTO>
    ${orderDTO.orderId}<br>
-->
