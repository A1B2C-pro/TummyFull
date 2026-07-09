// OTP verification page: auto-advance between boxes + 5 minute countdown timer

(function () {
  document.addEventListener('DOMContentLoaded', function () {
    var boxes = document.querySelectorAll('.otp-box');
    var hiddenInput = document.getElementById('otpValue');

    boxes.forEach(function (box, idx) {
      box.addEventListener('input', function () {
        box.value = box.value.replace(/[^0-9]/g, '').slice(0, 1);
        if (box.value && idx < boxes.length - 1) boxes[idx + 1].focus();
        syncHiddenValue();
      });
      box.addEventListener('keydown', function (e) {
        if (e.key === 'Backspace' && !box.value && idx > 0) boxes[idx - 1].focus();
      });
      box.addEventListener('paste', function (e) {
        e.preventDefault();
        var text = (e.clipboardData || window.clipboardData).getData('text').replace(/[^0-9]/g, '');
        for (var i = 0; i < boxes.length; i++) boxes[i].value = text[i] || '';
        syncHiddenValue();
        boxes[Math.min(text.length, boxes.length - 1)].focus();
      });
    });

    function syncHiddenValue() {
      if (!hiddenInput) return;
      var val = '';
      boxes.forEach(function (b) { val += b.value; });
      hiddenInput.value = val;
    }

    // Countdown timer (5 minutes) + enable resend button when expired
    var timerEl = document.getElementById('otpCountdown');
    var resendBtn = document.getElementById('resendBtn');
    if (timerEl) {
      var seconds = 5 * 60;
      var interval = setInterval(function () {
        seconds--;
        var m = Math.floor(seconds / 60);
        var s = seconds % 60;
        timerEl.textContent = m + ':' + (s < 10 ? '0' : '') + s;
        if (seconds <= 0) {
          clearInterval(interval);
          timerEl.textContent = '0:00';
          if (resendBtn) resendBtn.disabled = false;
        }
      }, 1000);
    }
  });
})();
