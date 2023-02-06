let begin = 1674897292103;
// const ServerData = new Array(1000).fill(1).map((v, i) => {
//   begin += Math.floor(7200000 * Math.random());
//   return {
//     uuid: i,
//     qq: 920868587,
//     time: begin,
//     message: `我说${i}`,
//   };
// });
const ServerData = []
const Data = [];
const selectQQ = []
let selectStoryName =1
const selectedArr = new Proxy([], {
  set() {
    const result = Reflect.set(...arguments);
    if (!selectedArr.length) {
      Footer.style.display = "";
    } else {
      // let height = window.innerHeight + "px";
      Footer.style.display = "flex";
    }
    return result;
  },
});
const qqMap = new Map()
const IdMap = new Map();
window.Data = Data;
const getIntoData = () =>{
  let timpo = 0;
  ServerData.forEach((v) => {
    let between = v.time - timpo;
    if (between >= 1800000) {
      Data.push({
        uuid: v.uuid + "timestamp",
        time: v.time,
        message: `以下消息从${new Date(v.time).toLocaleString()}开始`,
      });
    }
    IdMap.set(v.uuid + "", v);
    if (qqMap.has(v.qq)){
      qqMap.get(v.qq).push(v.uuid)
    }else{
      qqMap.set(v.qq,[v.uuid])
    }
    timpo = v.time;
    Data.push(v);
  });
}
const List = document.getElementById("commits");
const FrontNode = document.getElementById("front");
const EndNode = document.getElementById("end");
const pageChange = {
  upper: -1,
  lowwer: 2147483647,
  // m: 100,
  // n: 75,
  m: 2147483647,
  n: 2147483647,
  nowIndex: 0,
  frontHeight: 0,
  endHeight: 0,
};
window.pageChange = pageChange;
const nowShow = [];
const DataMap = new WeakMap();
const TopMapArr = [];

const render = () => {
  const fragment = document.createDocumentFragment();
  Data.slice(0, pageChange.m).forEach((v) => {
    const node = CommitRender(v);
    DataMap.set(node, v);
    nowShow.push(node);
    fragment.append(node);
  });
  List.append(fragment);
  pageChange.nowIndex = 0;
  pageChange.endHeight=0;
  if (Data.length > pageChange.m) {
    pageChange.lowwer = nowShow[pageChange.n].offsetTop;
    // console.log(pageChange.lowwer)
  }
};

document.onscroll = throttle((e) => {
  let top = document.documentElement.scrollTop;
  // console.log(top)
  if (top >= pageChange.lowwer) {
    // console.log("down"+pageChange.lowwer)
    pageChange.upper = -1;
    pageChange.lowwer = 2147483647;
    const fragment = document.createDocumentFragment();
    const nodeBegin = pageChange.nowIndex + nowShow.length;
    Data.slice(nodeBegin, nodeBegin + pageChange.n).forEach((v) => {
      const node = CommitRender(v);
      DataMap.set(node, v);
      nowShow.push(node);
      fragment.append(node);
    });
    let removeLength = nowShow.length - pageChange.m;
    pageChange.nowIndex += removeLength;
    if (pageChange.endHeight > 0) {
      let height = 0;
      nowShow
        .slice(nowShow.length - removeLength, nowShow.length)
        .forEach((v) => {
          height += v.offsetHeight;
        });
      pageChange.endHeight -= height;
      EndNode.style.height = pageChange.endHeight + "px";
    }
    // while(removeLength--) {
    //     const node = nowShow.shift()
    //     // pageChange.frontHeight += node.offsetHeight
    //     const source = DataMap.get(node)
    //     // source.top = node.offsetTop
    //     source.top = node.offsetTop
    //     // FrontNode.style.height = `calc(var(--header-heigth) + ${pageChange.frontHeight}px)`
    //     // FrontNode.style.height = node.offsetTop+"px"
    //     TopMapArr[Data.indexOf(source)]=pageChange.frontHeight
    //     // node.remove()
    // }
    const tmpArr = nowShow.splice(0, removeLength);
    // console.log(tmpArr.length)
    tmpArr.forEach((v, i, a) => {
      const source = DataMap.get(v);
      TopMapArr[Data.indexOf(source)] = v.offsetTop;
      // console.log(v)
      source.top = v.offsetTop;
    });
    List.appendChild(fragment);
    FrontNode.style.height = nowShow[0].offsetTop + "px";
    tmpArr.forEach((v) => v.remove());
    // console.log(nowShow)
    if (Data.length > pageChange.m + pageChange.nowIndex) {
      pageChange.lowwer = nowShow[pageChange.n].offsetTop;
    }
    pageChange.upper = nowShow[0].offsetTop;
  } else if (top <= pageChange.upper) {
    pageChange.upper = -1;
    pageChange.lowwer = 2147483647;

    const nowTop = document.documentElement.scrollTop;
    const readPoint =
      ((TopMapArr.findIndex((v) => v > nowTop) + TopMapArr.length) %
        TopMapArr.length) -
      1;
    if (TopMapArr.length - readPoint > pageChange.n) {
      let nodeBegin = 0;
      if (readPoint > pageChange.n) nodeBegin = readPoint - pageChange.n;
      let nodeEnd = nodeBegin + pageChange.m;
      let keepNode = nodeEnd - pageChange.nowIndex;
      if (keepNode <= 0) {
        keepNode = 0;
      } else {
        nodeEnd = pageChange.nowIndex;
      }
      while (keepNode < nowShow.length) {
        // const node = nowShow.pop()
        nowShow.pop().remove();
      }
      pageChange.nowIndex = nodeBegin;
      const fragment = document.createDocumentFragment();
      const tmpArr = [];
      Data.slice(nodeBegin, nodeEnd).forEach((v) => {
        const node = CommitRender(v);
        DataMap.set(node, v);
        tmpArr.push(node);
        fragment.append(node);
      });
      List.insertBefore(fragment, nowShow[0]);
      nowShow.unshift(...tmpArr);
      FrontNode.style.height = TopMapArr[pageChange.nowIndex] + "px";
    } else {
      const fragment = document.createDocumentFragment();
      const nodeBegin =
        pageChange.nowIndex - pageChange.n > 0
          ? pageChange.nowIndex - pageChange.n
          : 0;
      const tmpArr = [];
      Data.slice(nodeBegin, pageChange.nowIndex).forEach((v) => {
        const node = CommitRender(v);
        DataMap.set(node, v);
        tmpArr.push(node);
        fragment.append(node);
      });
      nowShow.unshift(...tmpArr);
      let removeLength = nowShow.length - pageChange.m;
      List.insertBefore(fragment, nowShow[tmpArr.length]);
      FrontNode.style.height = DataMap.get(nowShow[0]).top + "px";
      pageChange.nowIndex -= removeLength;
      while (removeLength--) {
        const node = nowShow.pop();
        // pageChange.endHeight+=node.offsetHeight
        node.remove();
      }
      // EndNode.style.height = pageChange.endHeight + "px"
      // console.log(nowShow)
    }
    pageChange.frontHeight = TopMapArr[pageChange.nowIndex];
    if (pageChange.nowIndex > 0) {
      pageChange.upper = nowShow[0].offsetTop;
    } else {
      FrontNode.style.height = "";
    }
    pageChange.lowwer = nowShow[pageChange.n].offsetTop;
  }
}, 10);
// render();
// window.render = render

function CommitRender(commit) {
  const node = document.createElement("div");
  if ("qq" in commit) {
    // console.log(commit.uuid)
    // console.log(commit.uuid+" "+ selectedArr.indexOf(commit.uuid))
    node.className =
      selectedArr.indexOf(commit.uuid + "") === -1
        ? "commit-box unselect-box"
        : "commit-box selected-box";

    node.style.setProperty("--commit-font-color", "#fff");
    node.style.setProperty("--commit-bg-color", "#0ae");

    node.onclick = Select;
    node.innerHTML = `
        <div class="advator">
            <img src="http://q1.qlogo.cn/g?b=qq&nk=${commit.qq}&s=40" time="${commit.time}" />
            <span>${commit.qq}</span>
        </div>
        <div class="context">
            <div class="commit-context">
                <p>${commit.message}</p>
            </div>
            <div class="talk-tangle"></div>
        </div>
        `;
  } else {
    node.className = "time-box";
    node.innerHTML = `${commit.message}`;
  }
  node.id = commit.uuid;
  // <span>${new Date(commit.time).toLocaleString()}
  return node;
}

function Select() {
    const id = this.id;
    const index = selectedArr.indexOf(id);
    if (index === -1) {
      selectedArr.push(id);
      this.className = "commit-box selected-box";
    } else {
      selectedArr.splice(index, 1);
      this.className = "commit-box unselect-box";
    }
};

function throttle(fun, delay) {
  let canUse = true;
  return function () {
    if (canUse) {
      canUse = false;
      setTimeout(() => {
        canUse = true;
      }, delay);
      return fun.apply(this, arguments);
    }
  };
}

function debounce(fun, delay) {
  let timerId = null;
  return function () {
    const context = this;
    // console.log(timerId)
    timerId && clearTimeout(timerId);
    timerId = setTimeout(() => {
      fun.apply(context, arguments);
      timerId = null;
    }, delay);
  };
}

const app = document.querySelector("body");
// window.Msg = KMessage
const messageArr = [];
function KMessage(message, type = "primary", time = 3000) {
  let el = document.createElement("div");
  el.className = "kmsg-" + type + " k-message color-btn";
  el.innerText = message;
  app.appendChild(el);
  messageArr.unshift(el);
  messageArr.forEach((v, i, a) => {
    if (i > 0) {
      v.style.top = a[i].offsetHeight * i + 15 * i + 20 + "px";
    }
  });
  let begin = setTimeout(() => {
    el.style.opacity = 1;
    begin && clearTimeout(begin);
  }, 200);

  let end = setTimeout(() => {
    el.style.opacity = 0;
    el.style.transform = "translate(-50%,100%)";
    end && clearTimeout(end);
  }, time - 500);

  let timo = setTimeout(() => {
    app.removeChild(el);
    let num = messageArr.indexOf(el);
    messageArr.splice(num, 1);
    el = null;
    timo && clearTimeout(timo);
  }, time);
}

const Footer = document.querySelector("footer");

document.getElementById("qqSelect").onclick = debounce(()=>{
    const qqs = selectedArr.reduce((result,v)=>{
        const commit = IdMap.get(v)
        // console.log(result)
        if (result.indexOf(commit.qq)===-1){
            result.push(commit.qq)
        }
        return result
    },[])
    qqs.forEach(v=>{
        qqMap.get(v).forEach(v=>{
            const value = v+""
            selectedArr.indexOf(value)===-1&&selectedArr.push(value)
            let dom = document.getElementById(v)
            dom&&(dom.className = "commit-box selected-box")
        })
    })
    // console.log(selectedArr)
},10)

document.getElementById("copy").onclick = () => {
  let str = "";
  selectedArr.forEach((v) => {
    const commit = IdMap.get(v);
    str += `[${commit.qq}]: ${commit.message}\n`;
  });
  document.getElementById("copy").dataset.clipboardText = str;
  KMessage("复制成功，快去粘贴吧","success",3000)
};
new ClipboardJS("#copy");

document.getElementById("clear").onclick = () => {
    selectedArr.splice(0,selectedArr.length).forEach(v=>{
        const dom  = document.getElementById(v)
        dom&&(dom.className = "commit-box unselect-box")
    })
    KMessage("清除成功")
}

document.getElementById("getall").onclick = debounce(()=>{
  selectedArr.splice(0,selectedArr.length)
  IdMap.forEach((v,k)=>{
    selectedArr.push(k+"")
  })
  nowShow.forEach(v=>{
    v.className==="commit-box unselect-box"&&(v.className = "commit-box selected-box")
  })
},50)

let style = "dark"
const colorSet = () => {
  const type = localStorage.getItem("showtype")
  if (type) {
    const options = JSON.parse(type)
    style = options.type
    const dom = document.documentElement
    for(const key in options){
      if (key!==type){
        dom.style.setProperty(key,options[key])
      }
    }
  }
}
colorSet()

document.getElementById("showtype").onclick = () => {
  const dom = document.documentElement
  let options
  if (style!=="dark"){
    options = {
      "--bg-color": "#000",
      "--font-color": "#eee",
      "--menu-shadow":"rgba(9,9,9,0.9)"
    }
    style = "dark"
  }else{
    options = {
      "--bg-color": "#fff",
      "--font-color": "#333",
      "--menu-shadow": "rgba(250,250,250,0.9)"
    }
    style = "day"
  }
  let showtype = Object.assign({type:style},options)
  // showtype.type = style
  localStorage.setItem("showtype",JSON.stringify(showtype))
  for(const key in options) {
    dom.style.setProperty(key,options[key])
  }
}

function getDataFromServer (url,data,type) {
  type&&(type = type.toLocaleUpperCase())
  const options = {
    method: type,
    headers: {
      'content-type':'application/json'
    }
  }
  if (type==="POST") {
    options.body = JSON.stringify(data)
  }else{
    url+="?"
    if(data)for(const key in data) {
      url+=`${key}=${data[key]}`
    }
  }
  return fetch(url,options).then(res=>{
    if(res.status===200){
      KMessage("获取数据成功","success",3000)
      return res
    }else{
      throw res.text()
    }
  }).catch(res=>{
    KMessage("Error:"+res,"danger",10000)
  })
}

function getStoryName() {
  return getDataFromServer("/getStoryNames").then(res=>res.json())
}

function getCommits(storyName) {
  return getDataFromServer("/getCommitsByStoryName",{storyname : storyName}).then(res=>res.json())
}

function Print(options) {
  return getDataFromServer("/download",
  options,
  "post"
  ).then(res=>res.blob()).then(blob=>{
    const link = document.createElement("a")
    const url = URL.createObjectURL(blob)
    link.href = url
    const date = new Date().toLocaleString().replace("/","年").replace("/","月").replace(" ","日").replace(":","时").replace(":","分")+"秒"
    link.download = `${options.storyname}--${date}.docx`;
    // document.body.appendChild(link);
    link.click();
    setTimeout(()=>{
      URL.revokeObjectURL(url)
    },3000)
  })
}

const StoryName = new Proxy([],{
  set(){
    const result = Reflect.set(...arguments)
    storyRender()
    return result
  }
})
const storyDom = document.querySelector(".menu-hover")
const storyDomChild = document.querySelector(".story-space")
function storyRender() {
  const fragment = document.createDocumentFragment()
  document.querySelectorAll(".story-container").forEach(v=>{
    v.remove()
  })
  StoryName.forEach(v=>{
    const node = StoryCreate(v)
    fragment.append(node)
  })
  storyDom.insertBefore(fragment,storyDomChild)
}

function storyClick(){
  const storyname = this.querySelector("span").innerText
  selectStoryName = storyname
  getCommits(storyname).then(res=>{
    ServerData.splice(0,ServerData.length,...res)
    selectedArr.splice(0,selectedArr.length)
    Data.splice(0,Data.length)
    nowShow.splice(0,nowShow.length)
    qqMap.clear()
    IdMap.clear()
    List.innerHTML = ""
    getIntoData()
    qqRender()
    render()
    document.getElementById("menu").checked = false
  })
}

function StoryCreate(storyname) {
  const node = document.createElement("div")
  node.onclick=storyClick
  node.className = "story-container story-checked"
  node.innerHTML=
  `<div><span>${storyname}</span></div>
  <span>${storyname}</span>`
  return node
}
getStoryName().then(res=>{
  StoryName.splice(0,StoryName.length,...res)
})

function qqRender() {
  const qqCheckNode = document.getElementById("qqcheck")
  qqCheckNode.innerHTML = ""
  selectQQ.splice(0,selectQQ.length)
  const fragment = document.createDocumentFragment()
  qqMap.forEach((v,k)=>{
    const node = document.createElement("label")
    node.setAttribute("for",k)
    node.className="qq-option"
    const nodeSign = document.createElement("span")
    nodeSign.innerText = k
    node.append(nodeSign)
    const nodeInput = document.createElement("input")
    node.append(nodeInput)
    nodeInput.type="checkbox"
    nodeInput.id=k
    nodeInput.checked=true
    nodeInput.onchange = qqCheck
    fragment.append(node)
    selectQQ.push(k)
  })
  qqCheckNode.append(fragment)
}

function qqCheck() {
  const index = selectQQ.indexOf(this.id-0)
  if (this.checked!==(index!==-1)) {
    this.checked?selectQQ.push(this.id-0):selectQQ.splice(index,1)
  }
  console.log(selectQQ)
}

const openFilter = () => {
  const dom = document.getElementById("filter-container")
  if (dom.style.opacity*1) {
    dom.style.opacity = 0
    dom.style.pointerEvents="none"
  }else{
    dom.style.opacity=1
    dom.style.pointerEvents="all"
  }
}

const FilterQQ = () => {
    let timpo = 0
    Data.splice(0,Data.length)
    nowShow.splice(0,nowShow.length)
    selectedArr.splice(0,selectedArr.length)

    ServerData.forEach((v) => {
      if (selectQQ.indexOf(v.qq)!==-1){
        let between = v.time - timpo;
        if (between >= 1800000) {
          Data.push({
            uuid: v.uuid + "timestamp",
            time: v.time,
            message: `以下消息从${new Date(v.time).toLocaleString()}开始`,
          });
        }
        timpo = v.time;
        Data.push(v);
      }

    });
    List.innerHTML = ""
    render()
}

const checkQQBySelecter = () => {
  selectQQ.forEach(v=>{
    qqMap.get(v).forEach(v=>{
        const value = v+""
        selectedArr.indexOf(value)===-1&&selectedArr.push(value)
        let dom = document.getElementById(v)
        dom&&(dom.className = "commit-box selected-box")
    })
  })
}

const returnToSource = () => {
  Data.splice(0,Data.length)
  nowShow.splice(0,nowShow.length)
  List.innerHTML = ""
  let timpo = 0;
  ServerData.forEach((v) => {
    let between = v.time - timpo;
    if (between >= 1800000) {
      Data.push({
        uuid: v.uuid + "timestamp",
        time: v.time,
        message: `以下消息从${new Date(v.time).toLocaleString()}开始`,
      });
    }
    timpo = v.time;
    Data.push(v);
  });  // qqRender()
  render()
}

const Search = ()=>{
  const input = document.getElementById("search")
  const time = new Date(input.value).getTime()
  for(let i=0;i<Data.length;i++){
    if (Data[i].time>time){
      const node = document.getElementById(Data[i].uuid)
      node.scrollIntoView({behavior: "smooth", block: "center", inline: "nearest"});
      node.className += " search-hover"
      openFilter()
      KMessage("查询到时间对应为"+new Date(Data[i].time).toLocaleString())
      setTimeout(() => {
        node.className = node.className.replace("search-hover","")
      },3000)
      break
    }
  }
}