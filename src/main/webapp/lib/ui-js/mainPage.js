/* global Ext:false */
Ext.onReady(function() {
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
													"background-color" : "#CBE0F7"
												},
												xtype : 'box',
												autoEl : {
													tag : 'img',
													src : './resources/aurin_logo.gif'
												}
											}, {
												region : 'east',
												xtype : 'label',
												text : 'Logout',
												margin : '60 50 0 0'
											}]
								},
								/*
								 * { region : 'north', html : "<img
								 * src=./resources/aurin_logo.gif>", height :
								 * 100,
								 * 
								 * bodyStyle : { "background-color" : "#CBE0F7",
								 * "text-align" : "left" }, border : false,
								 * margins : '0 0 0 0', tbar : [{ xtype :
								 * 'button', border : 0, text : 'test Button',
								 * style : { "background-color" : "#CBE0F7" },
								 * handler : function(btn) { alert('Button
								 * Click'); } }] },
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
									items : [developementPotential,
											developementAssessment]
								}]
					});
		});