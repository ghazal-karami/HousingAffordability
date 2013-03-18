/* global Ext:false */
Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.define('LinkButton', {
		extend : 'Ext.Component',
		alias : 'widget.linkbutton',
		renderTpl : '<div id="{id}-btnWrap" style="min-height:24px;" class="{baseCls}-linkbutton">'
				+ '<a id="{id}-btnEl" href="#" <tpl if="tabIndex"> tabIndex="{tabIndex}"</tpl> role="link">'
				+ '<tpl if="iconCls">'
				+ '<span style="min-width:18px;min-height:24px;float:left;" class="{iconCls}"></span>'
				+ '</tpl>'
				+ '<span id="{id}-btnInnerEl" class="{baseCls}-inner">'
				+ '{text}'
				+ '</span>'
				+ '<span id="{id}-btnIconEl" class="{baseCls}-icon"></span>'
				+ '</a>' + '</div>',
		renderSelectors : {
			linkEl : 'a'
		},
		initComponent : function() {
			this.callParent(arguments);
			this.renderData = {
				text : this.text,
				iconCls : this.iconCls
			};
		},
		listeners : {
			render : function(c) {
				c.el.on('click', c.handler);
			}
		},
		handler : function(e) {
		}
	});

	analyseBtn_DevelopPotential.disable();
	analyseBtn_DevelopAssessment.disable();
	Ext.Ajax.timeout = 14000000;

	alert("Welcome " + username);
	if (successStatus == "success") {
		analyseBtn_DevelopPotential.enable();
		analyseBtn_DevelopAssessment.enable();
	} else {
		alert(message);
	}
	Ext.create('Ext.container.Viewport', {
		layout : 'border',
		items : [{
			region : 'north',
			xtype : 'container',
			layout : 'border',
			height : 100,

			items : [{
						region : 'west',
						bodyStyle : {
							"background-color" : "#DFE8F6"
						},
						xtype : 'box',
						autoEl : {
							tag : 'img',
							src : './resources/aurin_logo.gif'
						}
					}, {
						region : 'east',
						// contentEl : 'f',
						html : '<div><a href="<c:url value="j_spring_security_logout" />">Logout</a></div>',
						width : 50,
						border : 0,
						bodyStyle : {
							"background-color" : "#DFE8F6"
						}
					}]
		}, {
			region : 'west',
			bodyStyle : {
				"background-color" : "#DFE8F6"
			},
			border : false,
			width : 0
		}, {
			region : 'south',
			bodyStyle : {
				"background-color" : "#DFE8F6"
			},
			border : false,
			height : 1
		}, {
			region : 'east',
			bodyStyle : {
				"background-color" : "#DFE8F6"
			},
			border : false,
			width : 0
		}, {
			region : 'center',
			title : "Data Platform To Support Housing Analysis and Research",
			xtype : 'tabpanel',
			activeTab : 1,
			items : [developementPotential, developementAssessment]
		}]
	});
});

//var Runner = function() {
//	var f = function(v, pbar, btn, count, cb) {
//		return function() {
//			if (v > count) {
//				btn.dom.disabled = false;
//				cb();
//			} else {
//
//				// / i think this is the block that is either wrong or in the
//				// wrong place
//				curStatus = mystatus.toString();
//				Ext.Ajax.request({
//							url : './account/getStatus',
//							method : 'GET',
//							success : function(result, request) {
//								mystatus = Ext.decode(result.responseText).status;
//							},
//							failure : function(result, request) {
//								Ext.MessageBox.alert('Failed',
//										result.responseText);
//							}
//						});
//
//				if (curStatus != mystatus) {
//					pbar.updateProgress(v / count, 'Working on ' + mystatus
//									+ ' Step ' + v + ' of ' + count + '...');
//				}
//			}
//		}
//	};
//	return {
//		run : function(pbar, btn, count, cb) {
//			btn.dom.disabled = true;
//			var ms = 5000 / count;
//			for (var i = 1; i < (count + 2); i++) {
//				setTimeout(f(i, pbar, btn, count, cb), i * ms);
//			}
//		}
//	}
//
//}();