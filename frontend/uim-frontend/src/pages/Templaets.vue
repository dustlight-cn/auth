<template>
  <div class="q-pa-md">

    <q-page-sticky position="bottom-right" :offset="[18, 18]">
      <q-btn dense @click="addTemplate" color="primary" round icon="add"/>
    </q-page-sticky>
    <q-table
      flat
      :data="templates"
      :columns="columns"
      title=""
      :rows-per-page-options="[]"
      row-key="name"
      wrap-cells
    >
      <!--      <template v-slot:bottom>-->

      <!--      </template>-->
      <template v-slot:body="props">
        <q-tr :props="props">
          <q-td key="name" :props="props">
            <div v-html="props.row.name"></div>
            <q-popup-edit
              @save="(val,initVal)=>updateName(props.row,initVal)"
              buttons
              v-model="props.row.name"
            >
              <q-input v-model="props.row.name"/>
            </q-popup-edit>
          </q-td>

          <q-td key="text" :props="props">
            <div v-html="props.row.text"></div>
            <q-popup-edit
              @save="(val,initVal)=>updateText(props.row,initVal)"
              buttons
              v-model="props.row.text"
              label-set="保存"
              label-cancel="取消"
            >
              <q-editor v-model="props.row.text"
                        min-height="5rem"
                        autofocus
                        @keyup.enter.stop/>
            </q-popup-edit>
          </q-td>

          <q-td key="opt" :props="props">
            <q-btn @click="()=>deleteTemplate(props.row)" flat dense round color="red-4" icon="delete"/>
          </q-td>
        </q-tr>
      </template>
      <template v-slot:no-data="{ icon, message, filter }">
        <div class="full-width row flex-center text-grey q-gutter-sm">
          <q-icon size="2em" :name="filter ? 'filter_b_and_w' : icon"/>
          <span>
            似乎没有数据
          </span>
        </div>
      </template>
    </q-table>

  </div>
</template>

<script>
  import axios from 'axios'
  import qs from 'qs'

  export default {
    name: 'Templates',
    data() {
      return {
        templates: [],
        columns: [
          {name: 'name', style: 'min-width: 100px;', align: 'left', label: '模板名', field: 'name'},
          {name: 'text', align: 'left', label: '模板内容', field: 'text'},
          {name: 'opt', align: 'center', label: '操作'},
        ]
      }
    },
    mounted() {
      this.updateList();
    }
    ,
    methods: {
      updateList() {
        this.$q.loading.show()
        axios.get('/api/template/list')
          .then(res => {
            this.templates = res
          }).catch(e => {
          console.error(e);
        }).finally(() => {

          this.$q.loading.hide()
        })
      },
      updateName(obj, initVal) {
        this.$q.loading.show()
        let data = {name: obj.name};
        axios.post("/api/template/name/" + encodeURIComponent(obj.id), qs.stringify(data))
          .catch(e => {
            obj.name = initVal;
          }).finally(() => {
          this.$q.loading.hide()
        })
      }
      ,
      updateText(obj, initVal) {
        this.$q.loading.show()
        let data = {text: obj.text};
        axios.post("/api/template/text/" + encodeURIComponent(obj.name), qs.stringify(data))
          .catch(e => {
            obj.text = initVal;
          }).finally(() => {
          this.$q.loading.hide()
        })
      },
      deleteTemplate(obj) {
        this.$q.dialog({
          title: '确认删除',
          message: '是否要删除模板 "' + obj.name + '" ？',
          cancel: true,
          ok: {
            label: "删除",
            color: "red-5",
            flat: true
          },
          cancel: {
            label: "取消",
            flat: true
          }
        }).onOk(() => {
          this.$q.loading.show()
          axios.delete("/api/template/delete", {
            data: [obj.name]
          })
            .then(res => {
              this.updateList();
            })
            .catch(e => {
              obj.text = initVal;
              this.$q.loading.hide();
            })
        })
      },
      addTemplate() {
        this.$q.dialog({
          title: '创建模板',
          message: '请输入模板名',
          prompt: {
            model: '',
            type: 'text' // optional
          },
          cancel: true,
          ok: {label: "创建", flat: true},
          cancel: {label: "取消", flat: true}
        }).onOk(data => {
          if (data == null || data.trim().length == 0)
            return
          this.$q.loading.show()
          axios.post("/api/template/text/" + encodeURIComponent(data), qs.stringify({text: "模板\"" + data + "\"的内容"}))
            .then(res => {
              this.updateList();
            })
            .catch(e => {
              this.$q.loading.hide();
            })
        })
      }
    }

  }
</script>
