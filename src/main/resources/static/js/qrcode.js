$(function () {
    // If a value is one character, add a zero before it.
    function rset(s) {
        return ("" + s).length < 2 ? "0" + s : "" + s;
    }

    // Get the current local time in 24-hour notation (hh:mm:ss)
    function time() {
    var d = new Date();
    return (
    rset(d.getHours()) +
    ":" +
    rset(d.getMinutes()) +
    ":" +
    rset(d.getSeconds())
    );
}

    // Create the QRCode instance
    var qrcode = new QRCode("qrcode", {
    text: time(),
    width: 400,
    height: 400,
    colorDark: "#262626",
    colorLight: "#dcdcd2",
    correctLevel: QRCode.CorrectLevel.H
});

    +(function timer() {
    // Recreate the QR code
    qrcode.clear();
    qrcode.makeCode(time());

    setTimeout(timer, 1000);
})();
});