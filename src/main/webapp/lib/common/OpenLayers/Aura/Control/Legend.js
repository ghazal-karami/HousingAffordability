/* Copyright (c) 2006-2012 by Ivo Widjaja.
 * Published under the 2-clause BSD license.
 * See license.txt in the OpenLayers distribution or repository for the
 * full text of the license. */

/**
 * @requires OpenLayers/Control.js
 */

OpenLayers.Aura = OpenLayers.Aura || {};
OpenLayers.Aura.Control = OpenLayers.Aura.Control || {};

/**
 * Class: OpenLayers.Aura.Control.Legend
 * The legend control adds legends from layers to the map display.
 * It uses 'getLegend' method of each layer.
 *
 * Inherits from:
 *  - <OpenLayers.Control>
 */
OpenLayers.Aura.Control.Legend =
  OpenLayers.Class(OpenLayers.Control, {

    /**
     * Constructor: OpenLayers.Aura.Control.Legend
     *
     * Parameters:
     * options - {Object} Options for control.
     */
    inDrawing: false,

    /**
     * Method: destroy
     * Destroy control.
     */
    destroy: function() {
        this.map.events.un({
            "removelayer": this.updateLegend,
            "addlayer": this.updateLegend,
            "changelayer": this.updateLegend,
            "changebaselayer": this.updateLegend,
            scope: this
        });

        OpenLayers.Control.prototype.destroy.apply(this, arguments);
    },

    /**
     * Method: draw
     * Initialize control.
     *
     * Returns:
     * {DOMElement} A reference to the DIV DOMElement containing the control
     */
    draw: function() {
        OpenLayers.Control.prototype.draw.apply(this, arguments);

        this.map.events.on({
            'changebaselayer': this.updateLegend,
            'changelayer': this.updateLegend,
            'addlayer': this.updateLegend,
            'removelayer': this.updateLegend,
            scope: this
        });
        this.updateLegend();

        return this.div;
    },

    /**
     * Method: updateLegend
     * Update legend.
     */
    updateLegend: function() {
        //if (this.inDrawing)
        //  return;
        this.inDrawing = true;

        var legends = [];
        // clean up
        /*
        var children = this.div.childNodes;
        console.log('children LENGTH', children.length);
        for (var i = 0, len = children.length; i < len; i++) {
          console.log("REMOVING CHILD", children[i]);
          try {
            this.div.removeChild(children[i]);
          } catch(err) {
            // extjs drawing conflict with the deletion
            console.log("ERROR REMOVING CHILD", err);
            continue
          }
        }*/

        if (this.map && this.map.layers) {
            for(var i=0, len=this.map.layers.length; i<len; i++) {
                var layer = this.map.layers[i];
                if (layer.getLegend && layer.getVisibility()) {
                    legends.push( layer.getLegend() );
                }
            }
            console.log('LEGEND LENGTH', legends.length);
            // to do: if nothing do not show legend container
            var html = '';
            for(var i=0, len=legends.length; i<len; i++) {
                var legend = legends[i];
                if (legend) {
                  html += legend;
                  //legend(this.div);
                  //this.div.appendChild(legend);
                }
            }
            var divEl = new Ext.dom.Element(this.div);
            if (html == '') {
              divEl.hide();
            } else {
              this.div.innerHTML = html;
              divEl.show();
            }
        }
        this.inDrawing = false;
    },

    CLASS_NAME: "OpenLayers.Aura.Control.Legend"
});
