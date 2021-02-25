$('.form-head').click(function(){

    if($(this).closest('.grop-from').attr('id')=='signup'){
        $('.grop-from').attr('id' , 'phone');
        $('.icon-action').addClass('back');
    }
    else if($(this).closest('.grop-from').attr('id')=='success'){
        $('.grop-from').attr('id' , 'signup');
        $('input').val('');
    }

});


$('.form-action').click(function(){

    var form_id = $('.grop-from').attr('id');
    console.log(form_id);
    console.log($('#control-' + form_id).val());
    $('.icon-action').addClass('back');
    if($('#control-' + form_id).val() !== '') {
        switch (form_id) {
            case "phone" :
                form_id = "certNo";
                break;
            case "certNo" :
                form_id = "success";
                break;
            case "success" :
                form_id = "signup";
                break;
        }
        $('.icon-action').addClass('back');
    } else {
        switch (form_id) {
            case "phone" :
                form_id = "signup";
                $('.icon-action').removeClass('back');
                break;
            case "certNo" :
                form_id = "phone";
                break;
            case "success" :
                form_id = "signup";
                break;
        }
        $('.icon-action').removeClass('back');
    }

    $('.grop-from').attr('id' , form_id);

});

$('input').keyup(function(){
    $('.grop-from').removeClass('error');
    $('.error-text').fadeOut();

    if($(this).val()!==''){
        $('.icon-action').removeClass('back');
    }
    else{
        $('.icon-action').addClass('back');
    }


})