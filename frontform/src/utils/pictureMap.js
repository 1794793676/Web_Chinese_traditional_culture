import picCalligraphy from '../picture/华夏文脉数字展馆背景图-书法之美.png'
import picSolarTerms from '../picture/华夏文脉数字展馆背景图-二十四节气.png'
import picMortise from '../picture/华夏文脉数字展馆背景图-榫卯建构.png'
import picPeople from '../picture/华夏文脉数字展馆背景图-民为邦本.png'
import picFestival from '../picture/华夏文脉数字展馆背景图-端午年俗.png'
import picInnovation from '../picture/华夏文脉数字展馆背景图-革故鼎新.png'

const pictureModules = import.meta.glob('../picture/*.{png,jpg,jpeg,webp,svg}', {
  eager: true,
  import: 'default'
})

const findPictureByName = (keywords = []) => {
  const list = Object.entries(pictureModules)
  const target = list.find(([path]) => keywords.some((keyword) => path.includes(keyword)))
  return target?.[1]
}

export const pictures = {
  calligraphy: picCalligraphy,
  solarTerms: picSolarTerms,
  mortise: picMortise,
  people: picPeople,
  festival: picFestival,
  innovation: picInnovation,
  login: findPictureByName(['登录.png', 'login.png']) || picCalligraphy,
  register: findPictureByName(['注册.png', 'register.png']) || picFestival
}

export const loginAsideImage = pictures.login
export const registerAsideImage = pictures.register
export const fallbackArticleCover = pictures.innovation
export const fallbackDetailCover = pictures.people
export const dashboardHeroImage = pictures.solarTerms

export const categoryHeroMap = {
  thought: pictures.people,
  spirit: pictures.calligraphy,
  craft: pictures.mortise,
  festival: pictures.festival,
  science: pictures.innovation,
  ecology: pictures.solarTerms
}

export const getCategoryHeroImage = (slug = '') => categoryHeroMap[slug] || pictures.calligraphy

export const getFallbackCoverFromText = (text = '') => {
  if (text.includes('书法') || text.includes('精神')) return pictures.calligraphy
  if (text.includes('端午') || text.includes('节日')) return pictures.festival
  if (text.includes('革故鼎新') || text.includes('创新') || text.includes('科学')) return pictures.innovation
  if (text.includes('榫卯') || text.includes('器物') || text.includes('非遗')) return pictures.mortise
  if (text.includes('民为邦本') || text.includes('思想')) return pictures.people
  if (text.includes('节气') || text.includes('生态')) return pictures.solarTerms
  return fallbackArticleCover
}
