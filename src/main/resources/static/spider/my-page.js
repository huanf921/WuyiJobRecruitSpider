// 执行分页，生成页面效果
function generatePage(pageIndex) {
    // 1.获取分页数据
    var pageInfoRemote = getPageInfoRemote(pageIndex);
    // 2.填充表格
    fillTableBody(pageInfoRemote);
}

// 远程访问服务器端程序获取pageInfo数据
function getPageInfoRemote(pageIndex) {
    // console.log("pn "+pageNum+" is?"+isNaN(pageNum));
    // console.log("ps "+pageSize+" is?"+isNaN(pageSize));
    var ajaxRs = $.ajax({
        "url": "/job/showpage",
        "type": "post",
        "data": {
            "pageNum": pageIndex,
            "pageSize": window.pageSize,
            "keyword": window.keyword
        },
        "async": false,
        "dataType": "json"
    });
    var statusCode = ajaxRs.status;
    // 发生错误
    if (statusCode != 200) {
        layer.msg("服务器端程序调用失败！响应状态码是=" + statusCode + "说明信息=" + ajaxRs.statusText);
        return null;
    }
    // 如果响应状态码是200表示请求成功
    var result = ajaxRs.responseJSON;
    // 判断result是否成功
    if (result == null) {
        layer.msg("查询不到相应数据");
        return null;
    }

    // 返回pageInfo
    return result;
}

// 填充表格
function fillTableBody(pageInfo) {
    // 清除tbody中的旧的数据
    $("#jobPageBody").empty();
    // 为了搜索没有结果时不显示页码
    $("#Pagination").empty();
    // 判断pageInfo是否有效
    if (pageInfo == null || pageInfo == undefined || pageInfo.content.length == 0) {
        $("#jobPageBody").append("<tr><td colspan='8'>抱歉！没有查询到您要的数据！</td></tr>");
        return;
    }

    // 使用pageInfo的list属性填充tbody
    for (var i = 0; i < pageInfo.content.length; i++) {
        // console.log("开始第"+(i + 1)+"次填充tbody");
        var job = pageInfo.content[i];
        var jobId = job.id;
        var jobName = job.jobName;
        var companyName = job.companyName;
        var jobInfo = job.jobInfo;
        var companyInfo = job.companyInfo;
        var companyAddr = job.companyAddr;
        var time = job.time;
        var url = job.url;

        if (i == 0) {
            window.firstJobId = jobId;
        }
        var jobIdTd = "<td>" + jobId + "</td>"
        var jobNameTd = "<td><a href='" + url + "'>" + jobName + "</td>"
        var companyNameTd = "<td>" + companyName + "</td>"
        var jobInfoTd = "<td><span class='detail' id='jobInfo" + jobId + "'>详情</span>" + "</td>"
        let companyInfoTd = "<td><span class='detail' id='companyInfo" + jobId + "'>详情</span>" + "</td>";
        window.infoArr.push({"jobInfo": jobInfo, "companyInfo": companyInfo});
        var companyAddrTd = "<td>" + companyAddr + "</td>"
        var timeTd = "<td>" + time + "</td>"

        // 通过button标签的id属性把roleId值传递到button按钮的单击响应函数中
        var starBtn = "<button id='" + jobId + "' type='button' class='btn btn-success btn-xs checkBtn'><i class=' glyphicon glyphicon-check'></i></button>";
        var buttonTd = "<td>" + starBtn + "</td>";
        var tr = "<tr>" + jobIdTd + jobNameTd + companyNameTd + jobInfoTd + companyInfoTd + companyAddrTd + timeTd + buttonTd + "</tr>";
        $("#jobPageBody").append(tr);
    }
    // 生成分页导航条
    generateNavigator(pageInfo);
}

// 生成分页页码导航条
function generateNavigator(pageInfo) {
// 获取总记录数
    var totalRecord = pageInfo.totalElements;
// 声明相关属性
    var properties = {
        "num_edge_entries": 3,
        "num_display_entries": 5,
        "callback": paginationCallBack,
        "items_per_page": pageInfo.pageable.pageSize,
        // "current_page": pageInfo.pageable.pageNumber + 1,
        "prev_text": "上一页",
        "next_text": "下一页"
    }
// 调用 pagination()函数
    $("#Pagination").pagination(totalRecord, properties);
}

// 翻页时的回调函数
function paginationCallBack(index, jQuery) {
// 修改 window 对象的 pageNum 属性
//     window.pageNum = ++pageIndex;
//     console.log("pn "+window.pageNum + " is?" + isNaN(window.pageNum))
    // parseInt(window.pageNum)
    // console.log("pn "+window.pageNum + " is?" + isNaN(window.pageNum))

    // 调用分页函数
    generatePage(index);
    // 取消页码超链接的默认行为
    return false;
}