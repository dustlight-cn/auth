import {boot} from 'quasar/wrappers';
import messages from 'src/i18n';
import Vue, {ComponentOptions} from 'vue';
import VueI18n from 'vue-i18n';
import {Quasar} from 'quasar';

declare module 'vue/types/vue' {
  interface Vue {
    i18n: VueI18n;

    /**
     * 获取语言文本
     * @param context Vue上下文或者 $options 或者 前缀
     * @param key
     * @param notKey 是否不允许 key 和值一样
     */
    $tt(context: any, key: string, notKey: boolean): string | null;

    changeLanguage(lang: string): null,

    languages: string[]
  }
}

const t = function (key: string) {
  var values = [], len = arguments.length - 1;
  while (len-- > 0) values[len] = arguments[len + 1];
  // @ts-ignore
  return i18n._t.apply(i18n, [key, i18n.locale, i18n._getMessages(), this].concat(values))
};

const tt = function (context: string | Vue | ComponentOptions<Vue>, key: string, notKey?: boolean) {
  let k: string;
  if (context == null) {
    k = key;
  } else if (typeof context === "string") {
    if (key == null)
      k = context;
    else
      k = context.endsWith('.') ? context + key : context + "." + key;
  } else if (context instanceof Vue) {
    if (key == null)
      k = (<Vue>context).$options.name || key;
    else
      k = (((<Vue>context).$options.name || "") + "." + key)
  } else {
    let c = <ComponentOptions<Vue>>context;
    if (c == null || c.name == null)
      k = (key);
    else if (key == null)
      k = (c.name);
    else
      k = (c.name + "." + key);
  }
  let v = t(k);
  return notKey && v == k ? "" : v;
};

Vue.use(VueI18n);

export const i18n = new VueI18n({
  locale: "zh-hans",
  fallbackLocale: "zh-hans",
  messages
});

export default boot(({app, Vue}) => {
  // Set i18n instance on app
  app.i18n = i18n;
  Vue.prototype.$tt = tt;
  Vue.prototype.languages = ['zh-hans', 'en-us'];
  Vue.prototype.changeLanguage = (lang: string) => {
    if (lang.startsWith('zh'))
      lang = 'zh-hans';
    else if (lang.startsWith('en'))
      lang = 'en-us';
    else {
      console.error("Language '" + lang + "' is not support!");
      lang = 'zh-hans';
    }
    import(/* webpackInclude: /(zh-hans|en-us)\.js$/ */ 'quasar/lang/' + lang)
      .then(lang => Vue.prototype.$q.lang.set(lang.default));
    i18n.locale = lang;
    Vue.prototype.$q.localStorage.set("lang", lang);
  }
  let lang = Vue.prototype.$q.localStorage.getItem("lang") || Quasar.lang.getLocale() || 'zh-hans';
  Vue.prototype.changeLanguage(lang);
});
