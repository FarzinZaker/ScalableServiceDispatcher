/**
 * Created by root on 8/28/14.
 */
$.formUtils.addValidator({
    name: 'national-code',
    validatorFunction: function (value, $el, config, language, $form) {
        var nationalCode = value;
        if (!nationalCode || nationalCode == '')
            return true;

        if (nationalCode.length != 10)
            return false;

        if (/[^0-9]/g.test(nationalCode))
            return false;

        if (nationalCode == '0000000000' ||
            nationalCode == '1111111111' ||
            nationalCode == '2222222222' ||
            nationalCode == '3333333333' ||
            nationalCode == '4444444444' ||
            nationalCode == '5555555555' ||
            nationalCode == '6666666666' ||
            nationalCode == '7777777777' ||
            nationalCode == '8888888888' ||
            nationalCode == '9999999999')
            return false;


        var num0 = parseInt(nationalCode.charAt(0)) * 10;
        var num2 = parseInt(nationalCode.charAt(1)) * 9;
        var num3 = parseInt(nationalCode.charAt(2)) * 8;
        var num4 = parseInt(nationalCode.charAt(3)) * 7;
        var num5 = parseInt(nationalCode.charAt(4)) * 6;
        var num6 = parseInt(nationalCode.charAt(5)) * 5;
        var num7 = parseInt(nationalCode.charAt(6)) * 4;
        var num8 = parseInt(nationalCode.charAt(7)) * 3;
        var num9 = parseInt(nationalCode.charAt(8)) * 2;
        var a = parseInt(nationalCode.charAt(9));

        var b = (((((((num0 + num2) + num3) + num4) + num5) + num6) + num7) + num8) + num9;
        var c = b % 11;

        return (((c < 2) && (a == c)) || ((c >= 2) && ((11 - c) == a)));
    },
    errorMessage: 'کد ملی وارد شده اشتباه است',
    errorMessageKey: 'wrongNationalCode'
});