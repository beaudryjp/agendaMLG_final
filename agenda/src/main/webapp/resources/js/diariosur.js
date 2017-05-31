$(document).ready(function () {
    $(".searchBox").hide();
    $("#tab2-main").hide();
    $("#search_form_calendar").hide();
    $("#followEvents").hide();
    $("#likeEvents").hide();
    //$("#form_contactUs").validate();

    //Click event to scroll to top
    $('.scrollToTop').click(function () {
        $('html, body').animate({scrollTop: 0}, 800);
        return false;
    });
    $(".iconSearchW").hover(function () {
        $(".searchBox").slideDown('fast');
    });
    $(".searchBox").mouseleave(function () {
        $(".searchBox").slideUp('fast');
    });
    $("#normalMenuIcon").click(function () {
        $("#header-option-icons").hide("slow");
        $("#myTopnav").show("slow");
    });
    $("#sidebarMenuIcon").click(function () {
        $("#header-option-icons").show("slow");
        $("#myTopnav").hide("slow");
    });
    $("#myTopnav").hover(function () {
        /* Stuff to do when the mouse enters the element */
    }, function () {
        $(this).hide("slow");
    });
    $("#tab2-header").click(function () {
        $("#search_form_options").hide('fast');
        $("#search_form_calendar").show('fast');
        $("#tab1-main").hide('fast');
        $("#tab2-main").show('fast');
    });
    $("#tab1-header").click(function () {
        $("#tab2-main").hide('fast');
        $("#search_form_options").show('fast');
        $("#search_form_calendar").hide('fast');
        $("#tab1-main").show('fast');
    });

    $("#h2assist").click(function () {
        $("#likeEvents").slideUp('fast');
        $("#followEvents").slideUp('fast');
        $("#assistEvents").slideDown('fast');
    });

    $("#h2like").click(function () {
        $("#followEvents").slideUp('fast');
        $("#assistEvents").slideUp('fast');
        $("#likeEvents").slideDown('fast');
    });

    $("#h2follow").click(function () {
        $("#likeEvents").slideUp('fast');
        $("#assistEvents").slideUp('fast');
        $("#followEvents").slideDown('fast');
    });
});
