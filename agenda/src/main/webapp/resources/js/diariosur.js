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
        $("#search_form_options").hide('slow');
        $("#search_form_calendar").show('slow');
        $("#tab1-main").hide('slow');
        $("#tab2-main").show('slow');
    });
    $("#tab1-header").click(function () {
        $("#tab2-main").hide('slow');
        $("#search_form_options").show('slow');
        $("#search_form_calendar").hide('slow');
        $("#tab1-main").show('slow');
    });

    $("#h2assist").click(function () {
        $("#likeEvents").fadeOut();
        $("#followEvents").fadeOut();
        $("#assistEvents").fadeIn();
    });

    $("#h2like").click(function () {
        $("#followEvents").fadeOut();
        $("#assistEvents").fadeOut();
        $("#likeEvents").fadeIn();
    });

    $("#h2follow").click(function () {
        $("#likeEvents").fadeOut();
        $("#assistEvents").fadeOut();
        $("#followEvents").fadeIn();
    });
});
