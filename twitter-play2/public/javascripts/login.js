/**
 * Created by yuki.haneda on 2016/08/19.
 */
$(function () {

    $('form').validate({
        errorElement: 'p'
    });

    //rotate title logo
    var angle = 0;
    setInterval(function () {
        angle+=1;
        $('#eval').rotate(angle)
    },50);
});