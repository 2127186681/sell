<html>
<#include "../common/hander.ftl">
<body>
<div id="wrapper" class="toggled">

    <#--边栏sidebar-->
    <#include "../common/nav.ftl">

    <#--一些主要内容content-->
    <div id="page-content-wrapper">
        <div class="container">
            <div class="row clearfix">
                <div class="col-md-4 column">
                    <table class="table table-bordered">
                        <thead>
                        <tr>
                            <th>订单ID</th>
                            <th>订单总金额</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td>${orderDTO.getOrderId()}</td>
                            <td>${orderDTO.orderAmount}</td>
                        </tr>



                        </tbody>
                    </table>
                </div>

                <#--订单详情表格-->
                <div class="col-md-12 column">
                    <table class="table table-bordered">
                        <thead>
                        <tr>
                            <th>商品ID</th>
                            <th>商品名称</th>
                            <th>商品价格</th>
                            <th>商品数量</th>
                            <th>商品总额</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list orderDTO.orderDetailList as orderDeatil>
                            <tr>
                            <td>${orderDeatil.productId}</td>
                            <td>${orderDeatil.productName}</td>
                            <td>${orderDeatil.productPrice}</td>
                            <td>${orderDeatil.productQuantity}</td>
                            <td>${orderDeatil.productPrice * orderDeatil.productQuantity}</td>
                            </tr>
                        </#list>




                        </tbody>
                    </table>
                </div>
                <#--操作-->
                <div class="col-md-12 column">
                    <#if orderDTO.getOrderStatus() == 0>
                        <a href="/sell/seller/order/finish?orderId=${orderDTO.getOrderId()}" type="button" class="btn btn-sm btn-primary">完结订单</a>
                        <a href="/sell/seller/order/cancel?orderId=${orderDTO.getOrderId()}" type="button" class="btn btn-sm btn-danger">取消订单</a>
                    </#if>

                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>