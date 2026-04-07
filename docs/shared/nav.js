/**
 * Shared Navigation Component
 * Injects a fixed top navigation bar into every page of the
 * Game Algorithms showcase website.
 *
 * Features:
 *  - Active link detection based on current pathname
 *  - Mobile hamburger menu with dropdown
 *  - Hide-on-scroll-down / show-on-scroll-up behavior
 *  - Auto-injects shared CSS and Google Fonts if missing
 */
(function () {
  'use strict';

  // ----------------------------------------------------------------
  // 1. Inject external dependencies if not already present
  // ----------------------------------------------------------------

  // Shared stylesheet
  if (!document.querySelector('link[href*="shared/styles.css"]')) {
    var cssLink = document.createElement('link');
    cssLink.rel = 'stylesheet';
    // Resolve path relative to the document — works from any depth
    var depth = (function () {
      var path = window.location.pathname.replace(/\/game-algorithms-java\/?/, '');
      var segments = path.split('/').filter(Boolean);
      // If the last segment looks like a file, don't count it
      if (segments.length && segments[segments.length - 1].indexOf('.') !== -1) {
        segments.pop();
      }
      return segments.length;
    })();
    var prefix = depth === 0 ? './' : '../'.repeat(depth);
    cssLink.href = prefix + 'shared/styles.css';
    document.head.appendChild(cssLink);
  }

  // Google Fonts (Cormorant Garamond, Inter, DM Mono)
  if (!document.querySelector('link[href*="fonts.googleapis.com"]')) {
    var preconnect1 = document.createElement('link');
    preconnect1.rel = 'preconnect';
    preconnect1.href = 'https://fonts.googleapis.com';
    document.head.appendChild(preconnect1);

    var preconnect2 = document.createElement('link');
    preconnect2.rel = 'preconnect';
    preconnect2.href = 'https://fonts.gstatic.com';
    preconnect2.crossOrigin = 'anonymous';
    document.head.appendChild(preconnect2);

    var fontsLink = document.createElement('link');
    fontsLink.rel = 'stylesheet';
    fontsLink.href =
      'https://fonts.googleapis.com/css2?family=Cormorant+Garamond:ital,wght@0,300;0,400;0,500;1,300;1,400;1,500&family=DM+Mono:wght@300;400;500&family=Inter:wght@300;400;500;600&display=swap';
    document.head.appendChild(fontsLink);
  }

  // ----------------------------------------------------------------
  // 2. Build the navigation DOM
  // ----------------------------------------------------------------

  var NAV_LINKS = [
    { label: 'Home', href: '/game-algorithms-java/' },
    { label: 'N-Queens', href: '/game-algorithms-java/nqueens/' },
    { label: 'Connect 4', href: '/game-algorithms-java/connect4/' },
    { label: 'Mastermind', href: '/game-algorithms-java/mastermind/' },
    { label: 'Card Trick', href: '/game-algorithms-java/cardtrick/' },
    { label: 'Compare', href: '/game-algorithms-java/compare/' }
  ];

  // Create <nav class="site-nav">
  var nav = document.createElement('nav');
  nav.className = 'site-nav';

  // Inner wrapper
  var inner = document.createElement('div');
  inner.className = 'nav-inner';

  // Logo / home link
  var logo = document.createElement('a');
  logo.href = '/game-algorithms-java/';
  logo.className = 'nav-logo';

  var logoIcon = document.createElement('span');
  logoIcon.className = 'nav-logo-icon';
  logoIcon.textContent = '\u25B2'; // ▲
  logo.appendChild(logoIcon);
  logo.appendChild(document.createTextNode(' Game Algorithms Engine'));

  // Links container
  var linksDiv = document.createElement('div');
  linksDiv.className = 'nav-links';

  var currentPath = window.location.pathname;

  NAV_LINKS.forEach(function (item) {
    var a = document.createElement('a');
    a.href = item.href;
    a.textContent = item.label;

    // Active link detection — match with or without trailing slash
    var normalizedCurrent = currentPath.replace(/\/$/, '') || '/';
    var normalizedHref = item.href.replace(/\/$/, '') || '/';
    if (normalizedCurrent === normalizedHref) {
      a.classList.add('active');
    }

    // Close mobile menu when a link is clicked
    a.addEventListener('click', function () {
      nav.classList.remove('nav-open');
    });

    linksDiv.appendChild(a);
  });

  // Mobile hamburger button
  var toggleBtn = document.createElement('button');
  toggleBtn.className = 'nav-mobile-toggle';
  toggleBtn.setAttribute('aria-label', 'Toggle menu');
  toggleBtn.textContent = '\u2630'; // ☰

  toggleBtn.addEventListener('click', function () {
    nav.classList.toggle('nav-open');
  });

  // Assemble the nav
  inner.appendChild(logo);
  inner.appendChild(linksDiv);
  inner.appendChild(toggleBtn);
  nav.appendChild(inner);

  // ----------------------------------------------------------------
  // 3. Inject the nav into the page
  // ----------------------------------------------------------------

  // Insert as the first child of <body>
  document.body.insertBefore(nav, document.body.firstChild);

  // Offset body content so it isn't hidden behind the fixed nav
  document.body.style.paddingTop = '70px';

  // ----------------------------------------------------------------
  // 4. Scroll behavior — hide on scroll down, show on scroll up
  // ----------------------------------------------------------------

  var lastScrollY = window.scrollY;
  var ticking = false;

  function onScroll() {
    var currentScrollY = window.scrollY;

    if (currentScrollY > lastScrollY && currentScrollY > 70) {
      // Scrolling down past the nav height — hide
      nav.classList.add('nav-hidden');
      // Also close the mobile menu when hiding the nav
      nav.classList.remove('nav-open');
    } else {
      // Scrolling up — show
      nav.classList.remove('nav-hidden');
    }

    lastScrollY = currentScrollY;
    ticking = false;
  }

  window.addEventListener('scroll', function () {
    if (!ticking) {
      window.requestAnimationFrame(onScroll);
      ticking = true;
    }
  });
})();
