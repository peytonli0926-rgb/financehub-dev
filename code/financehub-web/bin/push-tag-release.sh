#!/bin/bash

#检查分支是否正确
git pull

#获取最近版本号
latelyTag=$(git describe --match "release-*" --abbrev=0 --tags $(git rev-list --tags --max-count=1))

#版本前缀
tagPre="release-"

today="$(date +%Y%m%d)"
tagDate=${latelyTag:5:8}
tagCount=${latelyTag:14}
now="$(date +%H%M)"
echo "最近版本："$latelyTag
echo "当前日期："$today
newTag=${tagPre}${today}'-'${now}

# newTag=${tagPre}${today}'-'
# if [ $today == $tagDate ]
# then
#     newTagCount=`expr $tagCount + 1`
#     newTag+=${newTagCount}
#     echo $newTag
# else
#     newTag+='1'
#     echo $newTag
# fi
echo "新的版本："$newTag

if git tag -l | grep -q $newTag;then
    echo $newTag"已存在"
    read -p "按任意键关闭" -n 1
    exit
fi

#打标签
git tag $newTag
#推送单个标签到远端
git push origin $newTag

echo "tag 推送成功"
read -p "按任意键关闭" -n 1
