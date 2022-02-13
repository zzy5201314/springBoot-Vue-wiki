<template>
<a-layout>
  <a-layout-sider width="200" style="background: #fff">
    <a-menu
      v-model:selectedKeys="selectedKeys2"
      v-model:openKeys="openKeys"
      mode="inline"
      :style="{ height: '100%', borderRight: 0 }"
			@click="handleClick"
    >
		<a-menu-item key="welcome">
				<MailOutLined></MailOutLined>
				<span>欢迎</span>
		</a-menu-item>
      <a-sub-menu v-for="(item,index) in level1" :key="item.id">
				<template v-slot:title>
					<span>
						<user-outLine></user-outLine>
						{{item.name}}
					</span>
				</template>
				<a-menu-item v-for="(child,index) in item.children" :key="child.id">
					<MailOutLined/>
					<span>{{child.name}}</span>
				</a-menu-item>
      </a-sub-menu>
    </a-menu>
  </a-layout-sider>
  <a-layout-content
    :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
  >
	<div class="welcome" v-show="isShowWelcome">
		<the-welcome></the-welcome>
	</div>
	
	 <a-list item-layout="vertical" size="large" :grid="{ gutter: 20, column: 3 }"
		:data-source="ebooks" v-show="!isShowWelcome">
	     <template #renderItem="{ item }">
	       <a-list-item key="item.name">
	         <template #actions>
						 <span>
                <component v-bind:is="'FileOutlined'" style="margin-right: 8px" />
                {{ item.docCount }}
              </span>
              <span>
                <component v-bind:is="'UserOutlined'" style="margin-right: 8px" />
                {{ item.viewCount }}
              </span>
              <span>
                <component v-bind:is="'LikeOutlined'" style="margin-right: 8px" />
                {{ item.voteCount }}
              </span>
	         </template>
	         <a-list-item-meta :description="item.description">
	           <template #title>
							 <router-link :to="'/doc?ebookId=' + item.id">
								 {{ item.name }}
							 </router-link>
	           </template>
	           <template #avatar><a-avatar :src="item.cover" /></template>
	         </a-list-item-meta>
	       </a-list-item>
	     </template>
	   </a-list>
  </a-layout-content>
</a-layout>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref} from 'vue';
import axios from 'axios';
	import {
		message
	} from 'ant-design-vue';	
import { Tool } from "@/util/tool";
import TheWelcome from	'@/components/the-welcome.vue';

export default defineComponent({
  name: 'Home',
	components: {
		TheWelcome
	},
	setup()	{	//一半里面定义参数
		const ebooks = ref();  //响应式数据，在js里面动态修改这里面的值
		const isShowWelcome = ref(true);//两个页面互斥的条件
		
		let categoryId2 = 0;
		
		const handelQueryEbook = () => {
			axios.get("/ebook/list",{
				params: {
					page: 1,
					size: 1000,
					categoryId2: categoryId2
				}
			})
			.then((response) => {
				const data = response.data;
				ebooks.value = data.content.list;
			});
		}
		
		const handleClick = (value: any) => {
			// console.log(value);
			
			// isShowWelcome.value = value.key === 'welcome';
			if(value.key === 'welcome') {
				isShowWelcome.value = true;
			} else {
				categoryId2 = value.key;
				isShowWelcome.value = false;
				handelQueryEbook();
			}
		};
		
		onMounted(() => {
			handleQueryCategory();
			// handelQueryEbook();
		});
		const actions: Record<string, string>[] = [
			{ type: 'StarOutlined', text: '156' },
			{ type: 'LikeOutlined', text: '156' },
			{ type: 'MessageOutlined', text: '2' },
		];
		
		/**
		 * 查询所有分类
		 */
		const level1 = ref();
		let categorys: any;
		const handleQueryCategory = () => {
			axios.get("/category/all").then((response) => {
				const data = response.data;
				if (data.success) {
				  categorys = data.content;
					console.log("原始数组：", categorys);
		
					level1.value = [];
					level1.value = Tool.array2Tree(categorys, 0);
					console.log("树形结构：", level1.value);
				} else {
					message.error(data.message);
				}
			})
		};
		return {
			ebooks,
			actions,
			level1,
			
			
			handleClick,
			
			isShowWelcome
		};
	}
});
</script>

<style scoped>
	.ant-avatar {
	    width: 50px;
	    height: 50px;
	    line-height: 50px;
	    border-radius: 8%;
			margin: 5px 0;
	}
</style>
