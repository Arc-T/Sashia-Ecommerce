import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/(app)/(authenticated)/dashboard/discounts/')({
  component: DiscountIndex,
})

function DiscountIndex() {
  return <div>Hello "/dashboard/discounts/"!</div>
}
