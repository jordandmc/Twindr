/**
 * Defines the javascript routes for use with testing. There's probably a better way to get these than copying and pasting
 * the generated routes. Until that way is found: IF YOU CHANGE THE JAVASCRIPT ROUTES copy http://localhost:9000/js/routes to this file
 */

var jsRoutes = {}; (function(_root){
var _nS = function(c,f,b){var e=c.split(f||"."),g=b||_root,d,a;for(d=0,a=e.length;d<a;d++){g=g[e[d]]=g[e[d]]||{}}return g}
var _qS = function(items){var qs = ''; for(var i=0;i<items.length;i++) {if(items[i]) qs += (qs ? '&' : '') + items[i]}; return qs ? ('?' + qs) : ''}
var _s = function(p,s){return p+((s===true||(s&&s.secure))?'s':'')+'://'}
var _wA = function(r){return {ajax:function(c){c=c||{};c.url=r.url;c.type=r.method;return jQuery.ajax(c)}, method:r.method,type:r.method,url:r.url,absoluteURL: function(s){return _s('http',s)+'localhost:9000'+r.url},webSocketURL: function(s){return _s('ws',s)+'localhost:9000'+r.url}}}
_nS('controllers.MatchingController'); _root.controllers.MatchingController.acceptMatch =
function(twitterName) {
return _wA({method:"GET", url:"/" + "ajax/acceptMatch" + _qS([(function(k,v) {return encodeURIComponent(k)+'='+encodeURIComponent(v)})("twitterName", twitterName)])})
}

_nS('controllers.MatchingController'); _root.controllers.MatchingController.getPotentialMatches =
function() {
return _wA({method:"GET", url:"/" + "ajax/requestMatches"})
}

_nS('controllers.MatchingController'); _root.controllers.MatchingController.rejectMatch =
function(twitterName) {
return _wA({method:"GET", url:"/" + "ajax/rejectMatch" + _qS([(function(k,v) {return encodeURIComponent(k)+'='+encodeURIComponent(v)})("twitterName", twitterName)])})
}

_nS('controllers.MatchingController'); _root.controllers.MatchingController.unmatch =
function(twitterName) {
return _wA({method:"GET", url:"/" + "ajax/unmatch" + _qS([(function(k,v) {return encodeURIComponent(k)+'='+encodeURIComponent(v)})("twitterName", twitterName)])})
}

_nS('controllers.MatchingController'); _root.controllers.MatchingController.updateGeolocation =
function(latitude,longitude) {
return _wA({method:"GET", url:"/" + "ajax/updateGeolocation" + _qS([(function(k,v) {return encodeURIComponent(k)+'='+encodeURIComponent(v)})("latitude", latitude), (function(k,v) {return encodeURIComponent(k)+'='+encodeURIComponent(v)})("longitude", longitude)])})
}

})(jsRoutes)