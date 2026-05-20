const API_ORIGIN = import.meta.env.VITE_API_ORIGIN || 'http://localhost:8080'

const normalizeApiAssetToFrontendAsset = (url) => {
  if (!/^https?:\/\//i.test(url)) return url
  try {
    const parsed = new URL(url)
    const apiOrigin = new URL(API_ORIGIN)
    if (parsed.origin === apiOrigin.origin && parsed.pathname.startsWith('/assets/')) {
      return `${parsed.pathname}${parsed.search}${parsed.hash}`
    }
  } catch {
    return url
  }
  return url
}

export const resolveMediaUrl = (url) => {
  if (!url) return ''

  const normalizedUrl = normalizeApiAssetToFrontendAsset(url)

  if (
    /^https?:\/\//i.test(normalizedUrl) ||
    normalizedUrl.startsWith('data:') ||
    normalizedUrl.startsWith('blob:')
  ) {
    return normalizedUrl
  }

  if (normalizedUrl.startsWith('/api/')) return normalizedUrl
  if (normalizedUrl.startsWith('/assets/')) return normalizedUrl
  if (normalizedUrl.startsWith('/')) return `${API_ORIGIN}${normalizedUrl}`
  return `${API_ORIGIN}/${normalizedUrl}`
}
