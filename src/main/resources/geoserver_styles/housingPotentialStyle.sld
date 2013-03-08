<?xml version="1.0" encoding="ISO-8859-1"?>
  <StyledLayerDescriptor version="1.0.0" 
    xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd" 
    xmlns="http://www.opengis.net/sld" 
    xmlns:ogc="http://www.opengis.net/ogc" 
    xmlns:xlink="http://www.w3.org/1999/xlink" 
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <NamedLayer>
      <Name>Attribute-based polygon</Name>
      <UserStyle>
        <Title>SLD Cook Book: Attribute-based polygon</Title>
        <FeatureTypeStyle>
           <Rule>
            <Name>No Overlay Checked</Name>
            <Title>No Overlay Checked</Title>
            <ogc:Filter>            
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>OverlaysNu</ogc:PropertyName>
                <ogc:Literal>-1</ogc:Literal>
              </ogc:PropertyIsEqualTo>
            </ogc:Filter>
            <PolygonSymbolizer>
              <Fill>
                <CssParameter name="fill">#FF0000</CssParameter>
              </Fill>
              <Stroke>
                <CssParameter name="stroke">#FF0000</CssParameter>
               <CssParameter name="stroke-width">1</CssParameter>
              </Stroke>
            </PolygonSymbolizer> 
          </Rule>
          
          <Rule>
            <Name>No Overlay</Name>
            <Title>No Overlay</Title>
            <ogc:Filter>            
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>OverlaysNu</ogc:PropertyName>
                <ogc:Literal>0</ogc:Literal>
              </ogc:PropertyIsEqualTo>
            </ogc:Filter>
            <PolygonSymbolizer>
              <Fill>
                <CssParameter name="fill">#00FF00</CssParameter>
              </Fill>
               <Stroke>
                 <CssParameter name="stroke">#FF0000</CssParameter>
               <CssParameter name="stroke-width">1</CssParameter>
              </Stroke>
            </PolygonSymbolizer> 
          </Rule>
          
           <Rule>
            <Name>1 Overlay</Name>
            <Title>1 Overlay</Title>
            <ogc:Filter>            
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>OverlaysNu</ogc:PropertyName>
                <ogc:Literal>1</ogc:Literal>
              </ogc:PropertyIsEqualTo>
            </ogc:Filter>
            <PolygonSymbolizer>
              <Fill>
                <CssParameter name="fill">#FFFF00</CssParameter>
              </Fill>
               <Stroke>
                 <CssParameter name="stroke">#FF0000</CssParameter>
               <CssParameter name="stroke-width">1</CssParameter>
              </Stroke>
            </PolygonSymbolizer> 
          </Rule>
          
          <Rule>
            <Name>2 Overlays</Name>
            <Title>2 Overlays</Title>
            <ogc:Filter>            
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>OverlaysNu</ogc:PropertyName>
                <ogc:Literal>2</ogc:Literal>
              </ogc:PropertyIsEqualTo>
            </ogc:Filter>
            <PolygonSymbolizer>
              <Fill>
                <CssParameter name="fill">#557FFF</CssParameter>
              </Fill>
              <Stroke>
                 <CssParameter name="stroke">#FF0000</CssParameter>
               <CssParameter name="stroke-width">1</CssParameter>
              </Stroke>
            </PolygonSymbolizer> 
          </Rule>
          
          <Rule>
            <Name>3 Overlays</Name>
            <Title>3 Overlays</Title>
            <ogc:Filter>            
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>OverlaysNu</ogc:PropertyName>
                <ogc:Literal>3</ogc:Literal>
              </ogc:PropertyIsEqualTo>
            </ogc:Filter>
            <PolygonSymbolizer>
              <Fill>
                <CssParameter name="fill">#00FFFF</CssParameter>
              </Fill>
              <Stroke>
                <CssParameter name="stroke">#FF0000</CssParameter>
               <CssParameter name="stroke-width">1</CssParameter>
              </Stroke>
            </PolygonSymbolizer> 
          </Rule>
          
          <Rule>
            <Name>4 Overlays</Name>
            <Title>4 Overlays</Title>
            <ogc:Filter>            
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>OverlaysNu</ogc:PropertyName>
                <ogc:Literal>4</ogc:Literal>
              </ogc:PropertyIsEqualTo>
            </ogc:Filter>
            <PolygonSymbolizer>
              <Fill>
                <CssParameter name="fill">#FF55FF</CssParameter>
              </Fill>
             <Stroke>
                <CssParameter name="stroke">#FF0000</CssParameter>
               <CssParameter name="stroke-width">1</CssParameter>
              </Stroke>
            </PolygonSymbolizer> 
          </Rule>
          
          <Rule>
            <Name>5 Overlays</Name>
            <Title>5 Overlays</Title>
            <ogc:Filter>            
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>OverlaysNu</ogc:PropertyName>
                <ogc:Literal>5</ogc:Literal>
              </ogc:PropertyIsEqualTo>
            </ogc:Filter>
            <PolygonSymbolizer>
              <Fill>
                <CssParameter name="fill">#00AAFF</CssParameter>
              </Fill>
              <Stroke>
                <CssParameter name="stroke">#FF0000</CssParameter>
               <CssParameter name="stroke-width">1</CssParameter>
              </Stroke>
            </PolygonSymbolizer> 
          </Rule>
          
          <Rule>
            <Name>6 Overlays</Name>
            <Title>6 Overlays</Title>
            <ogc:Filter>            
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>OverlaysNu</ogc:PropertyName>
                <ogc:Literal>6</ogc:Literal>
              </ogc:PropertyIsEqualTo>
            </ogc:Filter>
            <PolygonSymbolizer>
              <Fill>
                <CssParameter name="fill">#FF7F55</CssParameter>
              </Fill>
              <Stroke>
                <CssParameter name="stroke">#FF0000</CssParameter>
               <CssParameter name="stroke-width">1</CssParameter>
              </Stroke>
            </PolygonSymbolizer> 
          </Rule>
          
          <Rule>
            <Name>7 Overlays</Name>
            <Title>7 Overlays</Title>
            <ogc:Filter>            
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>OverlaysNu</ogc:PropertyName>
                <ogc:Literal>7</ogc:Literal>
              </ogc:PropertyIsEqualTo>
            </ogc:Filter>
            <PolygonSymbolizer>
              <Fill>
                <CssParameter name="fill">#AA00FF</CssParameter>
              </Fill>
              <Stroke>
                <CssParameter name="stroke">#FF0000</CssParameter>
               <CssParameter name="stroke-width">1</CssParameter>
              </Stroke>
            </PolygonSymbolizer> 
          </Rule>
          
          <Rule>
            <Name>8 Overlays</Name>
            <Title>8 Overlays</Title>
            <ogc:Filter>            
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>OverlaysNu</ogc:PropertyName>
                <ogc:Literal>8</ogc:Literal>
              </ogc:PropertyIsEqualTo>
            </ogc:Filter>
            <PolygonSymbolizer>
              <Fill>
                <CssParameter name="fill">#3D8952</CssParameter>
              </Fill>
              <Stroke>
                <CssParameter name="stroke">#FF0000</CssParameter>
               <CssParameter name="stroke-width">1</CssParameter>
              </Stroke>
            </PolygonSymbolizer> 
          </Rule>
          
          <Rule>
            <Name>9 Overlays</Name>
            <Title>9 Overlays</Title>
            <ogc:Filter>            
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>OverlaysNu</ogc:PropertyName>
                <ogc:Literal>9</ogc:Literal>
              </ogc:PropertyIsEqualTo>
            </ogc:Filter>
            <PolygonSymbolizer>
              <Fill>
                <CssParameter name="fill">#810606</CssParameter>
              </Fill>
              <Stroke>
                <CssParameter name="stroke">#FF0000</CssParameter>
               <CssParameter name="stroke-width">1</CssParameter>
              </Stroke>
            </PolygonSymbolizer> 
          </Rule>
          
           <Rule>
            <Name>10 Overlays</Name>
            <Title>10 Overlays</Title>
            <ogc:Filter>            
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>OverlaysNu</ogc:PropertyName>
                <ogc:Literal>10</ogc:Literal>
              </ogc:PropertyIsEqualTo>
            </ogc:Filter>
            <PolygonSymbolizer>
              <Fill>
                <CssParameter name="fill">#B7C910</CssParameter>
              </Fill>
              <Stroke>
                <CssParameter name="stroke">#FF0000</CssParameter>
               <CssParameter name="stroke-width">1</CssParameter>
              </Stroke>
            </PolygonSymbolizer> 
          </Rule>
          
          <Rule>
            <Name>11 Overlays</Name>
            <Title>11 Overlays</Title>
            <ogc:Filter>            
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>OverlaysNu</ogc:PropertyName>
                <ogc:Literal>11</ogc:Literal>
              </ogc:PropertyIsEqualTo>
            </ogc:Filter>
            <PolygonSymbolizer>
              <Fill>
                <CssParameter name="fill">#23D1A6</CssParameter>
              </Fill>
              <Stroke>
                <CssParameter name="stroke">#FF0000</CssParameter>
               <CssParameter name="stroke-width">1</CssParameter>
              </Stroke>
            </PolygonSymbolizer> 
          </Rule>
          
          <Rule>
            <Name>12 Overlays</Name>
            <Title>12 Overlays</Title>
            <ogc:Filter>            
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>OverlaysNu</ogc:PropertyName>
                <ogc:Literal>12</ogc:Literal>
              </ogc:PropertyIsEqualTo>
            </ogc:Filter>
            <PolygonSymbolizer>
              <Fill>
                <CssParameter name="fill">#5D4A56</CssParameter>
              </Fill>
              <Stroke>
                <CssParameter name="stroke">#FF0000</CssParameter>
               <CssParameter name="stroke-width">1</CssParameter>
              </Stroke>
            </PolygonSymbolizer> 
          </Rule>
          
           <Rule>
            <Name>13 Overlays</Name>
            <Title>13 Overlays</Title>
            <ogc:Filter>            
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>OverlaysNu</ogc:PropertyName>
                <ogc:Literal>13</ogc:Literal>
              </ogc:PropertyIsEqualTo>
            </ogc:Filter>
            <PolygonSymbolizer>
              <Fill>
                <CssParameter name="fill">#0031E9</CssParameter>
              </Fill>
              <Stroke>
                <CssParameter name="stroke">#FF0000</CssParameter>
               <CssParameter name="stroke-width">1</CssParameter>
              </Stroke>
            </PolygonSymbolizer> 
          </Rule>
          
           <Rule>
            <Name>14 Overlays</Name>
            <Title>14 Overlays</Title>
            <ogc:Filter>            
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>OverlaysNu</ogc:PropertyName>
                <ogc:Literal>14</ogc:Literal>
              </ogc:PropertyIsEqualTo>
            </ogc:Filter>
            <PolygonSymbolizer>
              <Fill>
                <CssParameter name="fill">#FFAF02</CssParameter>
              </Fill>
              <Stroke>
                <CssParameter name="stroke">#FF0000</CssParameter>
               <CssParameter name="stroke-width">1</CssParameter>
              </Stroke>
            </PolygonSymbolizer> 
          </Rule>
          
          
          
        </FeatureTypeStyle>
      </UserStyle>
    </NamedLayer>
  </StyledLayerDescriptor>