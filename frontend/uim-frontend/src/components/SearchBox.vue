<template>
  <q-btn rounded flat icon="search" dense>
    <q-popup-edit @hide="()=>false" :validate="()=>false" :value="search">
      <q-input
        autofocus
        clearable
        clear-icon="close"
        :debounce="debounce"
        dense
        v-model="search"
        :placeholder="placeholder">
        <template v-slot:prepend>
          <q-icon v-if="isEmpty" name="search"/>
        </template>
      </q-input>
      <div class="q-mb-sm q-mt-sm text-grey">{{hint}}</div>
      <q-scroll-area v-if="!isEmpty && menus && menus.length > 0" style="height: 150px">
        <q-list separator>
          <q-item :to="menu.link" v-ripple clickable v-for="menu in this.menus">
            <q-item-section>
              <q-item-label>
                <q-icon v-if="menu.icon" :name="menu.icon" size="28px"/>
                {{menu.label}}
              </q-item-label>
            </q-item-section>
          </q-item>
        </q-list>
      </q-scroll-area>
    </q-popup-edit>
  </q-btn>
</template>
<script>
  import MenuConfig from './MenuConfig'

  export default {
    name: "SearchBox",
    props: {
      hasAuthorities: {
        type: Function,
        default: (authorities) => false
      }
    },
    data() {
      return {
        search: "",
        focused: false,
        standout: "bg-grey-7",
        placeholder: "请输入关键词",
        debounce: 500,
        menus: [],
        ms: true
      }
    },
    computed: {
      isEmpty() {
        return this.search == null || this.search.length <= 0
      },
      hint() {
        if (this.isEmpty)
          return ""
        if (this.menus == null || this.menus.length <= 0)
          return "无搜索结果"
        return "共有 " + this.menus.length + " 条搜索结果"
      }
    },
    methods: {
      isMatch(texts, keys) {
        for (let j in texts) {
          let text = texts[j]
          if (text == null)
            continue
          let t = text.toUpperCase().trim()
          if (t.length <= 0)
            continue
          let flag = false
          for (let i in keys) {
            if (keys[i] == null)
              continue
            let k = keys[i].trim().toUpperCase()
            if (k.length <= 0)
              continue
            if (t.indexOf(k) != -1)
              flag = true
            else {
              flag = false
              break
            }
          }
          if (flag)
            return true
        }
        return false
      },
      doSearch() {
        this.menus = []
        if (this.search == null || this.search == '')
          return
        let keys = this.search.split(' ')
        for (let i in MenuConfig) {
          let menus = MenuConfig[i]
          menus.forEach(menu => {
            if (menu.authorities && !this.hasAuthorities(menu.authorities) || !menu.link)
              return
            if (menu.label && this.isMatch([menu.label, menu.link, menu.keyword], keys)) {
              this.menus.push(menu)
            }
          })
        }
      }
    },
    watch: {
      search(val, initVal) {
        this.doSearch()
      }
    }
  }
</script>
