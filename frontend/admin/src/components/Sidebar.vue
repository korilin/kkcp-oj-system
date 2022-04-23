<script setup>
import { ref, watch } from 'vue';
import { HomeFilled, DashboardFilled, HighlightFilled, SnippetsFilled } from "@ant-design/icons-vue";
import { useRoute, useRouter } from 'vue-router';

const router = useRouter()
const route = useRoute()

const selectedKeys = ref(["profile"]);
const menus = [
  {
    key: "profile",
    icon: HomeFilled,
    text: "Profile",
  },
  {
    key: "contests",
    icon: HighlightFilled,
    text: "Contests",
  },
  {
    key: "questions",
    icon: SnippetsFilled,
    text: "Question Pool",
  },
]

function onSelect(item) {
  router.push({ name: item.key })
}

watch(() => route.name, (to) => {
  selectedKeys.value = [to];
})
</script>
<template>
  <a-menu
    v-model:selectedKeys="selectedKeys"
    mode="inline"
    :collapsedWidth="0"
    class="text-gray-12"
    @select="onSelect"
  >
    <template v-for="menu in menus" :key="menu.key">
      <div>
        <a-menu-item :key="menu.key">
          <component v-bind:is="menu.icon" />
          <span class="text">{{ menu.text }}</span>
        </a-menu-item>
      </div>
    </template>
  </a-menu>
</template>

<style scoped lang="scss">
.ant-menu-item {
  transition: all 0.3s;
  .anticon {
    padding: 8px 10px;
    box-shadow: 0 4px 6px rgb(0 0 0 / 12%);
    border-radius: 6px;
    color: #d9d9d9;
    background-color: white;
    transition: all 0.8s;
  }
  .text {
    user-select: none;
    color: #1f1f1f;
  }
  &:hover {
    .anticon {
      box-shadow: 0 2px 4px rgb(0 0 0 / 12%);
    }
  }
}

.ant-menu-item-selected {
  transition: all 0.1s;
  .anticon {
    color: white;
    background-color: #1890ff;
    transition: all 0.3s;
  }
  .text {
    font-weight: 600;
  }
}
</style>
