var table = new Tabulator("#income-table", {
    height:"311px",
    columns:[
        {title:"Source", field:"source", sorter:"string", editor:"input", width:250, formatter:"textarea", validator: ["unique", "required"]},
        {title:"Comments", field:"comments", editor:"textarea", width:1200, formatter: "textarea"},
        {title:"Amount (TL)", field:"amount", sorter:"number", bottomCalc:"sum", bottomCalcParams:{precision:3}, editor:"number", formatter:"money", validator: ["required", "min:0"]},
        {formatter:"rowSelection", titleFormatter:"rowSelection", headerSort:false},
    ],
});
//Add row on "Add Row" button click
$("#add-row_1").click(function(){
    table.addRow({});
});

//Delete row on "Delete Row" button click
$("#del-row_1").click(function(){
    var selectedRows = table.getSelectedRows();
    for(var i=0;i<selectedRows.length;i++)
    {
        selectedRows[i].delete();
    }
});

//Clear table on "Empty the table" button click
$("#clear_1").click(function(){
    table.clearData()
});

//Reset table contents on "Reset the table" button click
$("#reset_1").click(function(){
    table.setData(tabledata);
});

var table_2 = new Tabulator("#expenses-table", {
    height:"311px",
    columns:[
        {title:"Source", field:"source", sorter:"string", editor:"input", width:250, formatter:"textarea", validator: ["unique", "required"]},
        {title:"Comments", field:"comments", width:1200, editor:"textarea"},
        {title:"Amount (TL)", field:"amount", sorter:"number", bottomCalc:"sum", bottomCalcParams:{precision:3}, editor:"number", formatter:"money", validator: ["required", "min:0"]},
        {formatter:"rowSelection", titleFormatter:"rowSelection", align:"center", headerSort:false},
    ],
});
//Add row on "Add Row" button click
$("#add-row_2").click(function(){
    table_2.addRow({});
});

//Delete row on "Delete Row" button click
$("#del-row_2").click(function(){
    var selectedRows = table_2.getSelectedRows();
    for(var i=0;i<selectedRows.length;i++)
    {
        selectedRows[i].delete();
    }
});

//Clear table on "Empty the table" button click
$("#clear_2").click(function(){
    table_2.clearData()
});

//Reset table contents on "Reset the table" button click
$("#reset_2").click(function(){
    table_2.setData(tabledata);
});

/*//stringify a json object
$("#bruh").click(function(){
    var selectedRows = table_2.getSelectedRows();
    var total = 0;
    var str = "";
    for(var i=0;i<selectedRows.length;i++)
    {
    str = str + JSON.stringify(selectedRows[i].getData()) + "\n";
    }
    alert(str);
    for(var i=0;i<selectedRows.length;i++)
    {
        total += selectedRows[i].getData().amount;
    }
    alert(total);
});*/

$("#bruh").click(function(){
    var totalIncome = 0;
    var totalExpenses = 0;

    if(table.getCalcResults() != null)
        totalIncome = table.getCalcResults().bottom.amount;
    if(table_2.getCalcResults() != null)
        totalExpenses = table_2.getCalcResults().bottom.amount;

    var bln = totalIncome - totalExpenses;
    var str = "The balance is " + bln + " TL";

    document.getElementById("balance").innerHTML = str;
});

