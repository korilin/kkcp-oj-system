<script setup>
import * as monaco from "monaco-editor";
import editorWorker from "monaco-editor/esm/vs/editor/editor.worker?worker";
import { onMounted } from "vue";

const props = defineProps({
  code: String,
  onChange: Function,
});

self.MonacoEnvironment = {
  getWorker(_, label) {
    return new editorWorker();
  },
};

let editor = null;

onMounted(() => {
  const el = document.getElementById("editor");
  editor = monaco.editor.create(el, {
    value: props.code ?? "",
    language: "kotlin",
    scrollbar: {
      arrowSize: 5,
      vertical: "auto",
      horizontal: "hidden",
      verticalScrollbarSize: 5,
      horizontalHasArrows: false,
    },
    minimap: {
      enabled: false,
    },
    formatOnPaste: true,
    renderValidationDecorations: "on",
    scrollBeyondLastLine: false,
    autoIndent: true, // 自动布局
    roundedSelection: true,
  });
  editor.onDidChangeModelContent(() => {
    props.onChange(editor.getValue());
  });
});
</script>
<template>
  <div style="background-color: white; padding: 20px 5px">
    <div id="editor" ref="editor" style="height: 100%;"></div>
  </div>
</template>

<style lang="scss">
.monaco-editor,
.monaco-scrollable-element {
  width: 100% !important;
  height: 100% !important;
}

.overflow-guard {
  width: 100% !important;
  height: 100% !important;
}

.view-overlays,
.view-lines,
.current-line,
.lines-content {
  height: 100% !important;
}

.decorationsOverviewRuler {
  display: none;
}
</style>
