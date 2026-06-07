#!/usr/bin/env python3
"""Update code selection JSON with model choices."""
import json

with open("软件著作权申请资料/草稿/代码文件选择.json", "r", encoding="utf-8") as f:
    data = json.load(f)

# Define selected files and reasons
selections = {
    "frontend/src/main.js": "Vue应用入口文件，注册全局组件、插件和路由，体现软件整体架构",
    "frontend/src/App.vue": "根组件，包含全局导航和页面布局，展示软件整体UI结构",
    "frontend/src/router/index.js": "Vue Router路由配置，定义全部6个页面路由及懒加载，体现软件页面组织结构",
    "frontend/src/api/dream.js": "统一API封装，定义梦境CRUD、统计和AI对话的全部接口调用，体现前端与后端的数据交互逻辑",
    "frontend/src/views/Dashboard.vue": "首页仪表板页面，包含统计卡片、梦境录入表单和ECharts图表，是软件核心入口页面",
    "frontend/src/views/DreamList.vue": "梦境列表页面，包含分页浏览、性别/年龄/关键词筛选功能，体现数据浏览和过滤逻辑",
    "frontend/src/views/DreamDetail.vue": "梦境详情页面，展示梦境报告、人物分析、情感标签和AI分析入口，体现数据详情展示功能",
    "frontend/src/views/Search.vue": "高级搜索页面，支持多条件组合检索，体现软件的数据检索能力",
    "frontend/src/views/Statistics.vue": "数据统计页面，ECharts多维度图表展示，体现软件的数据可视化核心功能",
    "frontend/src/views/AIAnalysis.vue": "AI分析页面，包含梦境智能分析结果展示和AI对话功能，体现软件的核心AI能力",
}

total_lines = 0
selected_count = 0

for f in data["files"]:
    path = f["path"]
    if path in selections:
        f["selected"] = True
        f["model_reason"] = selections[path]
        selected_count += 1
        total_lines += f["line_count"]
    else:
        f["selected"] = False
        f["model_reason"] = ""

data["estimated_selected_lines"] = total_lines
data["estimated_selected_pages"] = total_lines // 50

print(f"Selected: {selected_count} files, {total_lines} lines, ~{total_lines // 50} pages")

with open("软件著作权申请资料/草稿/代码文件选择.json", "w", encoding="utf-8") as f:
    json.dump(data, f, ensure_ascii=False, indent=2)

print("OK")
