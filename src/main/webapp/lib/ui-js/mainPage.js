/* global Ext:false */
Ext.onReady(function() {

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
//								contentEl : 'f',
								html : '<div><a href="<c:url value="j_spring_security_logout" />">Logout</a></div>',
								width : 50,
								border : 0,
								bodyStyle : {
									"background-color" : "#DFE8F6"
								}

								// xtype : 'linkbutton',
							// text : 'Logout',
							// margin : '60 50 0 0',
							// handler : function() {
							// Ext.Ajax.request({
							// url : '/housing/logout',
							// method : 'GET',
							// success : function(response) {
							//
							// // var jresp = Ext.JSON
							// // .decode(response.responseText);
							// console.log('assessmentResponse'
							// + response.responseText);
							// window.location = 'ui-jsp/loginPage.jsp';
							// }
							//
							// });
							// }
						}]
				},
				/*
				 * { region : 'north', html : "<img
				 * src=./resources/aurin_logo.gif>", height : 100,
				 * 
				 * bodyStyle : { "background-color" : "#CBE0F7", "text-align" :
				 * "left" }, border : false, margins : '0 0 0 0', tbar : [{
				 * xtype : 'button', border : 0, text : 'test Button', style : {
				 * "background-color" : "#CBE0F7" }, handler : function(btn) {
				 * alert('Button Click'); } }] },
				 */{
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
					// TabPanel itself has no title
					activeTab : 1,
					// First tab active by default
					items : [developementPotential, developementAssessment]
				}]
	});
});