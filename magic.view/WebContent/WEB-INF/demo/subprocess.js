$(function(){  
	$('#subprocessTable').bootstrapTable({
		editable:true,//开启编辑模式  
        clickToSelect: true,  
        //cache: false,  
        //uniqueId: 'index', //将index列设为唯一索引  
        //striped: true,  
        //minimumCountColumns: 2,  
        smartDisplay:true,  
	    columns: [{
	        field: 'id',
	        title: 'Item ID'
	    }, {
	        field: 'name',
	        title: 'Item Name'
	    }, {
	        field: 'price',
	        title: 'Item Price'
	    }],
	    data: [{
	        id: 1,
	        name: 'Item 1',
	        price: '$1'
	    }, {
	        id: 2,
	        name: 'Item 2',
	        price: '$2'
	    }]
	});
});  