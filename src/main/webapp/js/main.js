// TummyFull - shared front-end behaviors (no framework, vanilla JS)

document.addEventListener('DOMContentLoaded', function () {
  // Auto-hide alert banners after 4s
  document.querySelectorAll('.alert').forEach(function (el) {
    setTimeout(function () { el.style.transition = 'opacity .4s'; el.style.opacity = '0'; setTimeout(function(){ el.remove(); }, 400); }, 4000);
  });

  // Category chip active state (client-side highlight only; filtering is server-driven via link params)
  document.querySelectorAll('.chip[data-filter]').forEach(function (chip) {
    chip.addEventListener('click', function () {
      document.querySelectorAll('.chip').forEach(c => c.classList.remove('active'));
      chip.classList.add('active');
    });
  });

  // Generic modal open/close (data-modal-target / data-modal-close)
  document.querySelectorAll('[data-modal-target]').forEach(function (btn) {
    btn.addEventListener('click', function () {
      var target = document.querySelector(btn.getAttribute('data-modal-target'));
      if (target) target.classList.add('open');
    });
  });
  document.querySelectorAll('[data-modal-close]').forEach(function (btn) {
    btn.addEventListener('click', function () {
      var modal = btn.closest('.modal-overlay');
      if (modal) modal.classList.remove('open');
    });
  });
  document.querySelectorAll('.modal-overlay').forEach(function (overlay) {
    overlay.addEventListener('click', function (e) {
      if (e.target === overlay) overlay.classList.remove('open');
    });
  });

  // Confirm before destructive actions
  document.querySelectorAll('[data-confirm]').forEach(function (form) {
    form.addEventListener('submit', function (e) {
      if (!confirm(form.getAttribute('data-confirm'))) e.preventDefault();
    });
  });

  // Payment option visual selection on checkout page
  document.querySelectorAll('.payment-option').forEach(function (opt) {
    opt.addEventListener('click', function () {
      document.querySelectorAll('.payment-option').forEach(o => o.classList.remove('selected'));
      opt.classList.add('selected');
      var radio = opt.querySelector('input[type=radio]');
      if (radio) radio.checked = true;
    });
  });
});
