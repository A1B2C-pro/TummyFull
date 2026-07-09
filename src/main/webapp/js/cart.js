// Menu page: local "+ / -" quantity UI before the Add to Cart POST is submitted.
// Each menu card is its own small form; increments only change the hidden
// qty field client-side, the real add/increase/decrease call still goes
// through CartServlet via normal form submission for correctness.

document.addEventListener('DOMContentLoaded', function () {
  document.querySelectorAll('.menu-add-form').forEach(function (form) {
    var stepper = form.querySelector('.qty-stepper');
    var display = form.querySelector('.qty-display');
    var minus = form.querySelector('.qty-minus');
    var plus = form.querySelector('.qty-plus');
    var actionInput = form.querySelector('input[name=action]');

    if (plus) {
      plus.addEventListener('click', function (e) {
        e.preventDefault();
        actionInput.value = 'increase';
        form.submit();
      });
    }
    if (minus) {
      minus.addEventListener('click', function (e) {
        e.preventDefault();
        actionInput.value = 'decrease';
        form.submit();
      });
    }
  });
});
