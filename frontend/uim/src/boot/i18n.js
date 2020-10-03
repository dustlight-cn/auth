import Vue from 'vue'
// 导入外部包
import VueI18n from 'vue-i18n'
import ZH_HANS from '../assets/lang/zh-hans.json'
import EN_US from '../assets/lang/en-us.json'

// 告诉Vue使用我们的Vue包:
Vue.use(VueI18n)
export default ({app, Vue}) => {
  // 在应用中设置i18n实例;
  // 我们通过这样做将它注入到根组件;
  // new Vue({..., i18n: ... }).$mount(...)

  app.i18n = new VueI18n({
    locale: 'zh-hans',
    fallbackLocale: 'en-us',
    messages: {
      'zh-hans': ZH_HANS,
      'en-us': EN_US
    }
  })
}
