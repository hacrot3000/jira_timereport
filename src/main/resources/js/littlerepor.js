(function(d){function h(a,b){this.h=d(a);this.g=d.extend({},k,b);this.U()}var k={containerHTML:'<div class="multi-select-container">',menuHTML:'<div class="multi-select-menu">',buttonHTML:'<span class="multi-select-button">',menuItemsHTML:'<div class="multi-select-menuitems">',menuFieldsetHTML:'<fieldset class="multi-select-fieldset">',menuFieldsetLegendHTML:'<legend class="multi-select-legend">',menuItemHTML:'<label class="multi-select-menuitem">',presetsHTML:'<div class="multi-select-presets">',
modalHTML:void 0,activeClass:"multi-select-container--open",noneText:"-- Select --",allText:void 0,presets:void 0,positionedMenuClass:"multi-select-container--positioned",positionMenuWithin:void 0,viewportBottomGutter:20,menuMinHeight:200};d.extend(h.prototype,{U:function(){this.J();this.S();this.L();this.K();this.M();this.P();this.V();this.W();this.T()},J:function(){if(!1===this.h.is("select[multiple]"))throw Error("$.multiSelect only works on <select multiple> elements");},S:function(){this.u=d('label[for="'+
this.h.attr("id")+'"]')},L:function(){this.j=d(this.g.containerHTML);this.h.data("multi-select-container",this.j);this.j.insertAfter(this.h)},K:function(){var a=this;this.l=d(this.g.buttonHTML);this.l.attr({role:"button","aria-haspopup":"true",tabindex:0,"aria-label":this.u.eq(0).text()}).on("keydown.multiselect",function(b){var c=b.which;13===c||32===c?(b.preventDefault(),a.l.click()):40===c?(b.preventDefault(),a.C(),(a.s||a.o).children().first().focus()):27===c&&a.m()}).on("click.multiselect",function(){a.D()}).on("blur.multiselect",
this.v.bind(this)).appendTo(this.j);this.h.on("change.multiselect",function(){a.G()});this.G()},G:function(){var a=[],b=[];this.h.find("option").each(function(){var c=d(this).text();a.push(c);d(this).is(":selected")&&b.push(d.trim(c))});this.l.empty();0==b.length?this.l.text(this.g.noneText):b.length===a.length&&this.g.allText?this.l.text(this.g.allText):this.l.text(b.join(", "))},M:function(){var a=this;this.i=d(this.g.menuHTML);this.i.attr({role:"menu"}).on("keyup.multiselect",function(b){27===
b.which&&(a.m(),a.l.focus())}).appendTo(this.j);this.N();this.g.presets&&this.R()},N:function(){var a=this;this.o=d(this.g.menuItemsHTML);this.i.append(this.o);this.h.on("change.multiselect",function(b,c){!0!==c&&a.H()});this.H()},H:function(){var a=this;this.o.empty();this.h.children("optgroup,option").each(function(b,c){"OPTION"===c.nodeName?(b=a.B(d(c),b),a.o.append(b)):a.O(d(c),b)})},F:function(a,b){a=b.which;38===a?(b.preventDefault(),b=b.currentTarget,(a=b.previousElementSibling)&&"LEGEND"===
a.nodeName&&(a=null),a||(a=b.parentNode.previousElementSibling),a?"LABEL"===a.nodeName?a.focus():a.lastChild.focus():this.l.focus()):40===a&&(b.preventDefault(),b=b.currentTarget,a=b.nextElementSibling,a||(a=b.parentNode.nextElementSibling),a&&("LABEL"===a.nodeName?a.focus():a.querySelector("label").focus()))},R:function(){var a=this;this.s=d(this.g.presetsHTML);this.i.prepend(this.s);d.each(this.g.presets,function(b,c){b=a.h.attr("name")+"_preset_"+b;var f=d(a.g.menuItemHTML).attr({"for":b,role:"menuitem"}).text(" "+
c.name).on("keydown.multiselect",a.F.bind(a,"preset")).appendTo(a.s);b=d("<input>").attr({type:"radio",name:a.h.attr("name")+"_presets",id:b}).prependTo(f);c.all&&(c.options=[],a.h.find("option").each(function(){var e=d(this).val();c.options.push(e)}));b.on("change.multiselect",function(){a.h.val(c.options);a.h.trigger("change")}).on("blur.multiselect",a.v.bind(a))});this.h.on("change.multiselect",function(){a.I()});this.I()},I:function(){var a=this;d.each(this.g.presets,function(b,c){b=a.h.attr("name")+
"_preset_"+b;b=a.s.find("#"+b);a:{c=c.options||[];var f=a.h.val()||[];if(c.length!=f.length)c=!1;else{c.sort();f.sort();for(var e=0;e<c.length;e++)if(c[e]!==f[e]){c=!1;break a}c=!0}}c?b.prop("checked",!0):b.prop("checked",!1)})},O:function(a,b){var c=this,f=d(c.g.menuFieldsetHTML),e=a.attr("label");f.attr("data-label",e);e=d(c.g.menuFieldsetLegendHTML).text(e);f.append(e);a.children("option").each(function(g,l){g=c.B(d(l),b+"_"+g);f.append(g)});c.o.append(f)},B:function(a,b){var c=this.h.attr("name")+
"_"+b;b=d(this.g.menuItemHTML).attr({"for":c,role:"menuitem"}).on("keydown.multiselect",this.F.bind(this,"menuitem")).text(" "+a.text());c=d("<input>").attr({type:"checkbox",id:c,value:a.val()}).prependTo(b);a.is(":disabled")&&c.attr("disabled","disabled");a.is(":selected")&&c.prop("checked","checked");c.on("change.multiselect",function(){d(this).prop("checked")?a.prop("selected",!0):a.prop("selected",!1);a.trigger("change",[!0])}).on("blur.multiselect",this.v.bind(this));return b},P:function(){var a=
this;this.g.modalHTML&&(this.A=d(this.g.modalHTML),this.A.on("click.multiselect",function(){a.m()}),this.A.insertBefore(this.i))},V:function(){var a=this;d("html").on("click.multiselect",function(){a.m()});this.j.on("click.multiselect",function(b){b.stopPropagation()})},W:function(){var a=this;this.u.on("click.multiselect",function(b){b.preventDefault();b.stopPropagation();a.D()})},T:function(){this.h.hide();this.u.removeAttr("for")},C:function(){d("html").trigger("click.multiselect");this.j.addClass(this.g.activeClass);
if(this.g.positionMenuWithin&&this.g.positionMenuWithin instanceof d){var a=this.i.offset().left+this.i.outerWidth(),b=this.g.positionMenuWithin.offset().left+this.g.positionMenuWithin.outerWidth();a>b&&(this.i.css("width",b-this.i.offset().left),this.j.addClass(this.g.positionedMenuClass))}a=this.i.offset().top+this.i.outerHeight();b=d(window).scrollTop()+d(window).height();a>b-this.g.viewportBottomGutter?this.i.css({maxHeight:Math.max(b-this.g.viewportBottomGutter-this.i.offset().top,this.g.menuMinHeight),
overflow:"scroll"}):this.i.css({maxHeight:"",overflow:""})},m:function(){this.j.removeClass(this.g.activeClass);this.j.removeClass(this.g.positionedMenuClass);this.i.css("width","auto")},D:function(){this.j.hasClass(this.g.activeClass)?this.m():this.C()},v:function(a){a.relatedTarget&&!d(a.relatedTarget).closest(this.j).length&&this.m()}});d.fn.multiSelect=function(a){return this.each(function(){d.data(this,"plugin_multiSelect")||d.data(this,"plugin_multiSelect",new h(this,a))})}})(jQuery);


function GetLastMonthStart() {
    return new Date(moment().subtract(1, 'months').startOf('month'));
}
function GetLastMonthEnd() {    
    return new Date(moment().subtract(1, 'months').endOf('month'));
}

function GetLastWeekStart() {
    return new Date(moment().subtract(1, 'weeks').startOf('isoWeek'));
}

function GetLastWeekEnd() {    
    return new Date(moment().subtract(1, 'weeks').endOf('isoWeek'));
}

function GetLastXDays(num) {    
    return new Date(moment().subtract(num, 'days'));
}

function GetWeekStart() {
    return new Date(moment().startOf('isoWeek'));
}
function GetWeekEnd() {    
    return new Date(moment().endOf('isoWeek'));
}

function GetMonthStart() {
    return new Date(moment().startOf('month'));
}
function GetMonthEnd() {    
    return new Date(moment().endOf('month'));
}

function setTimeToText(id, date)
{
    month = date.getMonth() + 1;
    
    if (month < 10)
        month = "0" + month;

    day = date.getDate()
    if (day < 10)
        day = "0" + day;

    $("#" + id).val(date.getFullYear() + "-" + month + "-" + day);

}