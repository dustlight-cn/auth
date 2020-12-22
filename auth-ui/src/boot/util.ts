import {boot} from "quasar/wrappers";

class Util {

  public dateFormat(date: Date | string, format: string) {
    if (!(date instanceof Date))
      date = new Date(date)
    let ret;
    if (format == null)
      format = "YYYY-mm-dd"
    const opt = {
      "Y+": date.getFullYear().toString(),        // 年
      "m+": (date.getMonth() + 1).toString(),     // 月
      "d+": date.getDate().toString(),            // 日
      "H+": date.getHours().toString(),           // 时
      "M+": date.getMinutes().toString(),         // 分
      "S+": date.getSeconds().toString()          // 秒
    };
    for (let k in opt) {
      ret = new RegExp("(" + k + ")").exec(format);
      if (ret) {
        // @ts-ignore
        format = format.replace(ret[1], (ret[1].length == 1) ? (opt[k]) : (opt[k].padStart(ret[1].length, "0")))
      }
      ;
    }
    ;
    return format;
  }
}

declare module 'vue/types/vue' {
  interface Vue {
    $util: Util;
  }
}

export default boot(({Vue}) => {
  Vue.prototype.$util = new Util();
})
