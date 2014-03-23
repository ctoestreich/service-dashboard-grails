$(document).ready(function(){
// Tooltips for widgets
    $('.widget [title]').tooltip({
        placement: 'right',
        container: 'body'
    });
// Tooltips for brand & nav toggle button
    $('.nav-toggle, .brand').tooltip({
        placement: 'bottom',
        container: 'body'
    });
// Tooltips
    $('[title]').tooltip({
        placement: 'right',
        container: 'body',
        delay: { show: 300, hide: 100 }
    });

    //Hidden elements
    $("body").tooltip({ selector: '[data-toggle="tooltip"]'});

// Close button for widgets
    var $widget = $('.widget');
    $widget.alert();
// Remove tooltip when widget is closed
    $widget.bind('close', function () {
        $(this).find('.close').tooltip('destroy');
    });
// Tabs
    $('.demoTabs a').click(function (e) {
        e.preventDefault();
        $(this).tab('show');
        $('.fullcalendar, .full-calendar-gcal').fullCalendar('render'); // Refresh jQuery FullCalendar for hidden tabs
    });
});