import { Settings as LayoutSettings } from '@ant-design/pro-components';

/**
 * @name
 */
const Settings: LayoutSettings & {
  pwa?: boolean;
  logo?: string;
} = 
{
  "navTheme": "light",
  "layout": "mix",
  "contentWidth": "Fluid",
   title:"Rapi接口",
  "fixedHeader": true,
  "fixSiderbar": true,
  "colorPrimary": "#722ED1",
  "splitMenus": false,
  "siderMenuType": "sub"
}
// {
//   navTheme: "light",
//   colorPrimary: "#1677FF",
//   layout: "side",
//   contentWidth: 'Fluid',
//   "siderMenuType": "sub",
//   fixedHeader: false,
//   fixSiderbar: true,
//   colorWeak: false,
//   title: 'Rapi接口',
//   pwa: false,
//   logo: 'https://gw.alipayobjects.com/zos/rmsportal/KDpgvguMpGfqaHPjicRK.svg',
//   iconfontUrl: '',

  
// };

export default Settings;
