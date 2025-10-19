/**
 * 
 */// sidebar.js - small helper to toggle sidebars on mobile
(function(){
  function toggle(id){
    var el = document.getElementById(id);
    if(!el) return;
    if(el.style.display === 'none' || window.getComputedStyle(el).display === 'none'){
      el.style.display = 'block';
    } else {
      el.style.display = 'none';
    }
  }
  window.ABC = window.ABC || {};
  window.ABC.toggleLeft = function(){ toggle('left-sidebar'); };
  window.ABC.toggleRight = function(){ toggle('right-sidebar'); };
})();