/**
 *
 * Dependencies:
 * <script type="text/javascript" src="../js/janky.post/janky.post.js"></script>
 */
Ext.ns("Aura.data");

Ext.define('Aura.data.Consumer', {
  requires: ["Aura.Util", "Aura.Cfg"],
  singleton: true,

  /**
   *
   * Example:
   * getSimpleJsonP('http://115.146.90.141:2000/feature/lga/2006?action=getstore',
   *   function (jsonData) {
   *     console.log(jsonData);
   *   });
   */
  getSimpleJsonP: function (url, handler) {

    var dummyStore = Ext.create('Ext.data.Store', {
      model: "Aura.Util.dummyJsonP",
      proxy:
      { type: 'jsonp'
      , url: url
      , reader: {
          type: 'json'
        }
      },
      listeners: {
        load: function (store, records) {
          handler(store.proxy.reader.rawData);
        }
      },
      autoLoad: true
    });
  }

  /** Example:
      var serviceParams =
      { "xdomain": "cors"
      , "url" : "http://115.146.90.140:8080/aurin-datasource-client/getDatasetAttributes"
      , "method": "post"
      , "params":
        { "dataSourceURI": "http://aurin-1.rc.melbourne.nectar.org.au:4567/datasources/Landgate/LandgateWfsABS"
        , "name": "slip:ABS-073"
        }
      };
      function myHandler(r) {
        console.log(r, typeof r);
      }
      getProxiedService(serviceParams, myHandler, 0)
  */

, getBridgedService: function (serviceParams, handler, noXhr, raw) {
    if (Aura.useDispatcher) {
      Aura.data.Consumer.dispatcherService(serviceParams, handler, noXhr, raw);
    } else {
      Aura.data.Consumer.xdomainService(serviceParams, handler, noXhr, raw);
    }
  }

, dispatcherService: function (serviceParams, handler, noXhr, raw) {
    // dispatcher is in the same domain with the HTML container
    var strParams = JSON.stringify(serviceParams);
    var x;

    if (XMLHttpRequest) {
      var xhr = new XMLHttpRequest();
      xhr.open('POST', "../dispatcher", true);
      xhr.setRequestHeader("Content-Type", "application/json");
      xhr.onreadystatechange = function (evXhr) {
        if (xhr.status == 204) {
          handler((raw) ? '' : {});
          return;
        }
        if (xhr.readyState == 4 && xhr.status == 200) {
          if (raw) {
            handler( xhr.responseText);
          } else {
            var result;
            try {
              result = JSON.parse(xhr.responseText);
            } catch (err) {
              result = xhr.responseText;
            }
            handler(result);
          }
        }
      };
      xhr.send(strParams);
    } else {
      _.log(this, 'use XMLHttpRequest-enabled browser')
    }
  } // end of dispatcherService

, xdomainService: function (serviceParams, handler, noXhr, raw) {
    // noXhr = true: force to use xdomain POST
    var params = serviceParams;
    var strParams = JSON.stringify(params);
    var xDomainEnabled = false;
    console.log(strParams);

    if (!noXhr && XMLHttpRequest) {
      var xhr = new XMLHttpRequest();
      if ("withCredentials" in xhr) { // CORS is supported in XMLHttpRequest
         // Firefox 3.5 and Safari 4
        xhr.open('POST', Aura.Cfg.endpoints.xdomainBridge, true);

        //Send the proper header information along with the xhr
        xhr.setRequestHeader("Content-type", "application/json");
        xhr.onreadystatechange = function (evXhr) {
          if (xhr.status == 204) { // service with no response
            if (raw) {
              handler('');
            } else {
              handler({});
            }
            return;
          }
          if (xhr.readyState == 4 && xhr.status == 200) {
            if (raw) {
              handler(xhr.responseText);
            } else {
              handler(JSON.parse(xhr.responseText));
            }

            return;
            // TO DO check below
            var result;
            try {
              result = JSON.parse(xhr.responseText);
            } catch (err) {
              result = xhr.responseText;
            }
            handler(result);
          }
        };
        _.log(this, strParams);
        xhr.send(strParams);
        xDomainEnabled = true;
      } else if (window.XDomainRequest) {
        // IE8
        var xdr = new window.XDomainRequest();
        xdr.open("POST", Aura.Cfg.endpoints.xdomainBridge);
        xdr.onload = function () {
            if (raw) {
              handler(xdr.responseText);
              return;
            }
            var result;


            try {
              result = JSON.parse(xdr.responseText);
            } catch (err) {
              result = xdr.responseText;
            }
            handler(result);
        };
        xdr.send(strParams);
        xDomainEnabled = true;
      }
    }
    if (!xDomainEnabled) {
      // Browser's XHR does not support CORS
      // Handle accordingly using janky

      janky(
        { url: Aura.Cfg.endpoints.xdomainBridge
        , data: serviceParams
        , method: "post"
        , success: function (response) {
            handler(JSON.parse(response));
            // need to parse janky output
            console.log(response);
          }
        , error: function () {
            console.log(error);
        }
      });
    }
  } // end of xdomainService

});